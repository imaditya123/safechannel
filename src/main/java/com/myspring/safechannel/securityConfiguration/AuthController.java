package com.myspring.safechannel.securityConfiguration;

import java.time.Instant;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;

@RestController
public class AuthController {
	
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthController.class);

	private JwtEncoder jwtEncoder;
	private JwtDecoder jwtDecoder;
	private DataSource dataSource;

	public AuthController(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder,DataSource dataSource) {
		super();
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
		this.dataSource=dataSource;
	}

	@PostMapping("/auth/login")
	public JwtRespose authenticate(@RequestBody UserRequestModel userRequestModel) {
		return new JwtRespose(createToken(userRequestModel));
	}
	
	
	@PostMapping("/auth/signin")
	public JwtRespose signIn(@RequestBody UserRequestModel  userRequestModel) {
		var user = User.withUsername(userRequestModel.getUsername())
				
				.password(userRequestModel.getPassword()).passwordEncoder(str -> passwordEncoder().encode(str)).roles("USER").build();
		
		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.createUser(user);
		
		return new JwtRespose(createToken(userRequestModel));
		
	}
	



	private String createToken(UserRequestModel userRequestModel) {
		var claims = JwtClaimsSet.builder().issuer("self").issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(60 * 30)).subject(userRequestModel.getUsername())
				.claim("scope", createScope(userRequestModel)).build();
		
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	private String createScope(UserRequestModel userRequestModel) {
		return userRequestModel.getRole().stream().collect(Collectors.joining(" "));
	}
	
	
	private BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

record JwtRespose(String token) {
}
