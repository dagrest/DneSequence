package com.dagrest.dnesequence.util;

import com.dagrest.dnesequence.exception.InvalidRootNode;
import com.dagrest.dnesequence.exception.NonIntegerValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class Util {
	
	public static List<Integer> ParseRequestBody(String json) throws JsonMappingException, JsonProcessingException, InvalidRootNode, NonIntegerValue {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(json);
		
		if (rootNode.has("seq") && rootNode.get("seq").isArray()) {
		    List<Integer> seqList = new ArrayList<Integer>();
		    for (JsonNode node : rootNode.get("seq")) {
		        if (node.isInt()) {
		            seqList.add(node.asInt());
		        } else {
					throw new NonIntegerValue("incorrect request body: provided array contains non-integer value.");
		        }
		    }
		    return seqList;
		} else {
			throw new InvalidRootNode("incorrect request body: invalid name of the root node or provided value is not an array.");
		}
	}
	
	public static boolean CheckDneSequence(List<Integer> inputList) {
		
		int inputListLength = inputList.size();
		Integer[] inputArray = inputList.toArray(new Integer[inputListLength]);
		
	    int i = -1, j = -1, k = -1;
	    
	    for (int m = 0; m < inputListLength; m++) {
	    	
	        if (i == -1 || inputArray[m] < inputArray[i]) {
	            i = m;
	        } else if (j == -1 || inputArray[m] > inputArray[j]) {
	            j = m;
	        } else if (k == -1 || inputArray[m] > inputArray[k]) {
	            k = m;
	        }
	        
	        if (i != -1 && j != -1 && k != -1 && 
	        	i < j && j < k && 
	        	inputArray[i] < inputArray[k] && inputArray[k] < inputArray[j]) {
	        	return true; // DNE sequence found
	        }
	    }
	    return false; // DNE sequence not found
	}

}
