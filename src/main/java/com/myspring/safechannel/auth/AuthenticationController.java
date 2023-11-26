package com.myspring.safechannel.auth;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	final Logger log= LoggerFactory.getLogger(getClass());

  private final AuthenticationService service;

  @PostMapping("/register")

  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) throws Exception {
	  try {
		  return ResponseEntity.ok(service.register(request));
		
	} catch (Exception e) {
		log.info("Error Recieved");
	
		throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"User Already exists");
	} 
   
  }
  @PostMapping("/authenticate")
  public ResponseEntity authenticate(
      @RequestBody AuthenticationRequest request
  ) {
	  try {
		  log.info("request data {}",request.toString());
		  return ResponseEntity.ok(service.authenticate(request));
		
	} catch (Exception e) {
		log.info("request data {}",e.getMessage().toString());
		 return new  ResponseEntity(HttpStatus.BAD_REQUEST);
		// TODO: handle exception
	}
	  
   
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}