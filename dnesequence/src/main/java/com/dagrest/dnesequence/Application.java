package com.dagrest.dnesequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.dagrest.dnesequence")
public class Application {

    private static final Logger logger = LoggerFactory.getLogger("com.example.MyController");

	public static void main(String[] args) {
		logger.info("Application started.");
		SpringApplication.run(Application.class, args);
	}

}
