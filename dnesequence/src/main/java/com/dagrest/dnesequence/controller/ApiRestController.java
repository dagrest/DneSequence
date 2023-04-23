package com.dagrest.dnesequence.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dagrest.dnesequence.exception.InvalidRootNode;
import com.dagrest.dnesequence.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class ApiRestController {
	
	  private static final Logger logger = LoggerFactory.getLogger("com.dagrest.dnesequence.controller.ApiRestController");
	  
	  @PostMapping("/server")
	  public ResponseEntity<String> handlePostRequest(@RequestBody String requestBody) {
		  
		  try {
			  
			  List<Integer> inputArray = Util.ParseRequestBody(requestBody);
			  if(inputArray == null) {
				  logger.error("Invalid request body: " + requestBody);
				  return (ResponseEntity<String>) ResponseEntity.badRequest().body("Invalid request body.");
			  }
			  String isDneSequenceExist = Util.CheckDneSequence(inputArray) == true ? "true" : "false";
			  logger.info("Is DNE Sequence exist? - " + isDneSequenceExist);
			  return ResponseEntity.ok(isDneSequenceExist);
			  
		  } catch (JsonMappingException e) {
			  logger.error(e.getMessage());
			  return (ResponseEntity<String>) ResponseEntity.badRequest().body(e.getMessage());
		  } catch (JsonProcessingException e) {
			  logger.error(e.getMessage());
			  return (ResponseEntity<String>) ResponseEntity.badRequest().body(e.getMessage());
		  } catch (InvalidRootNode e) {
			  logger.error(e.getMessage());
			  return (ResponseEntity<String>) ResponseEntity.badRequest().body(e.getMessage());
		  }
	  }
	  
	  @GetMapping("/health")
	  public ResponseEntity<String> myEndpoint() {
		  logger.info("DNE service is running OK.");
	      return ResponseEntity.ok("OK");
	  }
}
