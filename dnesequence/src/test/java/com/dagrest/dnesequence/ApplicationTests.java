package com.dagrest.dnesequence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonParseException;
import com.dagrest.dnesequence.exception.InvalidRootNode;
import com.dagrest.dnesequence.util.Util;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	// Integration test for GET "/health" endpoint with mock
	@Test
	public void healthTest() throws Exception {
		mockMvc.perform(get("/health"))
			.andExpect(status().isOk())
			.andExpect(content().string("OK"));
	}
			
    static Stream<Map.Entry<String, String>> requestBodyProvider() {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("{\"seq\": [1, 2, 3]}", "false");
        requestBodyMap.put("{\"seq\": [1, 3, 2]}", "true");        
        requestBodyMap.put("{\"seq\": [1, 2, 3, 7]}", "false");
        requestBodyMap.put("{\"seq\": [4, 1, 7, 8, 7, 2]}", "true");
        return requestBodyMap.entrySet().stream();
    }
    
    // Integration test for POST "/server" endpoint with mock
	@ParameterizedTest
    @MethodSource("requestBodyProvider")
    public void checkDneSequenceIntegrationTest(Map.Entry<String, String> entry) throws Exception {
        String requestBody = entry.getKey();
        String expectedResponse = entry.getValue();
        
		mockMvc.perform(MockMvcRequestBuilders.post("/server")
        		.contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }
	
	// NEGATIVE: Prepare test case data for unit test for "util.ParseRequestBody" method
    private static Stream<Arguments> negativerequestBodyProvider() {
        return Stream.of(
            Map.of(
                "inputListString", "{\"s\": [1, 2, 3]}",
                "expectedException", InvalidRootNode.class,
                "expectedResponse", "incorrect request body: invalid name of the root node or provided value is not an array."
            ),
            Map.of(
                "inputListString", "{\"seq\": [1, 3, e]}",
                "expectedException", JsonParseException.class,
                "expectedResponse", "Unrecognized token 'e': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n"
                		+ " at [Source: (String)\"{\"seq\": [1, 3, e]}\"; line: 1, column: 17]"
            )
        ).map(Arguments::of);
    }
    
    // NEGATIVE: Unit test for "util.ParseRequestBody" method
	@ParameterizedTest
    @MethodSource("negativerequestBodyProvider")
    public void negativeCheckDneSequenceIntegrationTest(Map<String, Object> testCase) throws Exception {
        String inputListString = (String) testCase.get("inputListString");
        Class<?> expectedException = (Class<?>) testCase.get("expectedException");
        String expectedResponse = (String) testCase.get("expectedResponse");   
        
        if(expectedException.equals(InvalidRootNode.class)) {
    		mockMvc.perform(MockMvcRequestBuilders.post("/server")
            		.contentType(MediaType.APPLICATION_JSON)
                    .content(inputListString))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
        }else {
    		mockMvc.perform(MockMvcRequestBuilders.post("/server")
            		.contentType(MediaType.APPLICATION_JSON)
                    .content(inputListString))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
        }
    }

	// Prepare test case data for unit test for "util.ParseRequestBody" method
    static Stream<Map.Entry<String, List<Integer>>> testCaseValuesParseRequestBody() {
        Map<String, List<Integer>> testCaseValuesMap = new HashMap<>();        
        testCaseValuesMap.put("{\"seq\": [1, 2, 3]}", convertStringListToInteger("1,2,3"));
        testCaseValuesMap.put("{\"seq\": [1, 3, 2]}", convertStringListToInteger("1,3,2"));        
        testCaseValuesMap.put("{\"seq\": [1, 2, 3, 7]}", convertStringListToInteger("1,2,3,7"));
        testCaseValuesMap.put("{\"seq\": [4, 1, 7, 8, 7, 2]}", convertStringListToInteger("4,1,7,8,7,2"));
        return testCaseValuesMap.entrySet().stream();
    }
    
    // Unit test for "util.ParseRequestBody" method
	@ParameterizedTest
    @MethodSource("testCaseValuesParseRequestBody")
    public void checkParseRequestBodyUnitTest(Map.Entry<String, List<Integer>> entry) throws Exception {
        String inputListString = entry.getKey();    
        List<Integer> expectedOutput = entry.getValue();    

        List<Integer> actualOutput = Util.ParseRequestBody(inputListString);
        assertEquals(expectedOutput, actualOutput);
    }
    
	// NEGATIVE: Prepare test case data for unit test for "util.ParseRequestBody" method
    private static Stream<Arguments> negativeTestCaseValuesParseRequestBody() {
        return Stream.of(
            Map.of(
                "inputListString", "{\"s\": [1, 2, 3]}",
                "expectedException", InvalidRootNode.class,
                "expectedMessage", "incorrect request body: invalid name of the root node or provided value is not an array."
            ),
            Map.of(
                "inputListString", "{\"seq\": [1, 3, e]}",
                "expectedException", JsonParseException.class,
                "expectedMessage", "Unrecognized token 'e': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n"
                		+ " at [Source: (String)\"{\"seq\": [1, 3, e]}\"; line: 1, column: 17]"
            )
        ).map(Arguments::of);
    }
    
    // NEGATIVE: Unit test for "util.ParseRequestBody" method
	@ParameterizedTest
    @MethodSource("negativeTestCaseValuesParseRequestBody")
    public void checkParseRequestBodyNegativeUnitTest(Map<String, Object> testCase) throws Exception {
        String inputListString = (String) testCase.get("inputListString");
        Class<?> expectedException = (Class<?>) testCase.get("expectedException");
        String expectedMessage = (String) testCase.get("expectedMessage");   
        
        if(expectedException.equals(InvalidRootNode.class)) {
        	Exception exception = assertThrows(InvalidRootNode.class, () -> {
            	Util.ParseRequestBody(inputListString);
        	});
            assertEquals(expectedMessage, exception.getMessage());
        }else {
        	Exception exception = assertThrows(JsonParseException.class, () -> {
            	Util.ParseRequestBody(inputListString);
        	});     
            assertEquals(expectedMessage, exception.getMessage());
        }
    }
	
	// Prepare test case data for unit test for "util.checkDneSequence" method
    static Stream<Map.Entry<String, Boolean>> testCaseValues() {
        Map<String, Boolean> testCaseValuesMap = new HashMap<>();        
        testCaseValuesMap.put("1,2,3", false);
        testCaseValuesMap.put("1,3,2", true);        
        testCaseValuesMap.put("1,2,3,7", false);
        testCaseValuesMap.put("4,1,7,8,7,2", true);
        return testCaseValuesMap.entrySet().stream();
    }
    
    static List<Integer> convertStringListToInteger(String inputListString){
    	List<Integer> inputList = new ArrayList<>();
    	for (String s : inputListString.split(",")) {
    		inputList.add(Integer.parseInt(s.trim()));
    	}
    	return inputList;
    }
    
    // Unit test for "util.checkDneSequence" method
	@ParameterizedTest
    @MethodSource("testCaseValues")
    public void checkDneSequenceUnitTest(Map.Entry<String, Boolean> entry) throws Exception {
        String inputListString = entry.getKey();
        Boolean expectedResult = entry.getValue();       
        List<Integer> inputList = convertStringListToInteger(inputListString);

        boolean expectedOutput = expectedResult;
        boolean actualOutput = Util.CheckDneSequence(inputList);
        assertEquals(expectedOutput, actualOutput);
    }
	
	
}
