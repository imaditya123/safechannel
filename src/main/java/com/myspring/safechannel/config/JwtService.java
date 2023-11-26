package com.myspring.safechannel.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class JwtService {

	private final Environment environment;
//	private @Value("${application.security.jwt.secret-key}") String secretKey;

//	private @Value("${application.security.jwt.expiration}") long jwtExpiration;

//	private @Value("${application.security.jwt.refresh-token.expiration}") long refreshExpiration;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		long jwtExpiration = environment.getProperty("application.security.jwt.expiration", Long.class,
				Long.valueOf(0));

		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		long refreshExpiration = environment.getProperty("application.security.jwt.refresh-token.expiration",
				Long.class, Long.valueOf(0));

		return buildToken(new HashMap<>(), userDetails, refreshExpiration);
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
	
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {

		String secretKey = environment.getProperty("application.security.jwt.secret-key");

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}