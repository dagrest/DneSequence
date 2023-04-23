package com.dagrest.dnesequence.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dagrest.dnesequence.exception.InvalidRootNode;
import com.dagrest.dnesequence.exception.NonIntegerValue;
import com.dagrest.dnesequence.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class ApiRestController {
	  @PostMapping("/server")
	  public ResponseEntity<String> handlePostRequest(@RequestBody String requestBody) {
		  
		  try {
			  
			  List<Integer> inputArray = Util.ParseRequestBody(requestBody);
			  if(inputArray == null) {
				  return (ResponseEntity<String>) ResponseEntity.badRequest().body("Invalid request body.");
			  }
			  return ResponseEntity.ok(Util.CheckDneSequence(inputArray) == true ? "true" : "false");
			  
		  } catch (JsonMappingException e) {
			  return (ResponseEntity<String>) ResponseEntity.badRequest().body(e.getMessage());
		  } catch (JsonProcessingException e) {
			  return (ResponseEntity<String>) ResponseEntity.badRequest().body(e.getMessage());
		  } catch (InvalidRootNode e) {
			  return (ResponseEntity<String>) ResponseEntity.badRequest().body(e.getMessage());
		  } catch (NonIntegerValue e) {
			  return (ResponseEntity<String>) ResponseEntity.badRequest().body(e.getMessage());
		  }	
	  }
	  
	  @GetMapping("/health")
	  public ResponseEntity<String> myEndpoint() {
	      return ResponseEntity.ok("OK");
	  }
}
