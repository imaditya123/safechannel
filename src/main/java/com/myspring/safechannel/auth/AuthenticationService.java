package com.myspring.safechannel.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myspring.safechannel.config.JwtService;
import com.myspring.safechannel.token.Token;
import com.myspring.safechannel.token.TokenRepository;
import com.myspring.safechannel.token.TokenType;
import com.myspring.safechannel.user.Role;
import com.myspring.safechannel.user.User;
import com.myspring.safechannel.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository repository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	final Logger log= LoggerFactory.getLogger(AuthenticationService.class);
	public AuthenticationResponse register(RegisterRequest request) throws ResponseStatusException {
		

		var isUserExistist = repository.findByEmail(request.getEmail()).isPresent();
//		if (isUserExistist)
//			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already exists!");
		var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER).build();
		var savedUser = repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		saveUserToken(savedUser, jwtToken);
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		log.info("-1");
		log.info("User details {}",request.toString());
		
		
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		boolean isAuthenticated=passwordsMatch(user.getPassword(),request.getPassword());
		log.info("is Auth {}",isAuthenticated);
		if(!isAuthenticated) {
			
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User details not correct ");
		}
		log.info("2");
		var jwtToken = jwtService.generateToken(user);
		log.info("3");
		var refreshToken = jwtService.generateRefreshToken(user);
		log.info("4");
		revokeAllUserTokens(user);
		log.info("5");
		saveUserToken(user, jwtToken);
		log.info("6");
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	private void saveUserToken(User user, String jwtToken) {
		var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false)
				.build();
		tokenRepository.save(token);
	}

	private void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.repository.findByEmail(userEmail).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
	
	
	private boolean passwordsMatch(String savedPassword, String candidatePassword) {
		log.info("raw pass {}",passwordEncoder.encode(candidatePassword));
		log.info("saved pass {}",savedPassword);
	    return passwordEncoder.matches(candidatePassword, savedPassword);
	}

}