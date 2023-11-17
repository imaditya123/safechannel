package com.myspring.safechannel.resources;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import ch.qos.logback.classic.Logger;

@RestController
public class TodoResource {
	 private static final Logger logger = (Logger) LoggerFactory.getLogger(TodoResource.class);

	
	@GetMapping("/v1/hello")
	public String helloworld() {
		return "Hello World";
	}
	
	@PostMapping("/v1/hello")
	public String posthello(@RequestBody Map body) {
		logger.info("Hello post API{}",body.toString());
		return "Hello World";
	}
	
	

}
