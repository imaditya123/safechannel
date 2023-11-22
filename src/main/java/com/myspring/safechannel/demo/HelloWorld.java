package com.myspring.safechannel.demo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class HelloWorld {
	
	
	@GetMapping("/hello")
	public ResponseModel hello(){
		return new  ResponseModel("Hello World");
	}

}


record ResponseModel(String str) {}