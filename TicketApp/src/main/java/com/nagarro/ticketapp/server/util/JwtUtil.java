package com.nagarro.ticketapp.server.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.nagarro.ticketapp.server.model.*;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	private String SECRET_KEY = "secret";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, user);
	}
	
	private String createToken(Map<String, Object> claims, User user) {
		return Jwts.builder()
				.setClaims(claims)
				.claim("id", user.getEmail())
				.claim("BU", user.getBU())
				.claim("Address1", user.getAdd1())
				.claim("Address2", user.getAdd2())
				.claim("admin", user.isAdmin())
				.claim("city", user.getCity())
				.claim("state", user.getState())
				.claim("country", user.getCountry())
				.claim("firstName", user.getfName())
				.claim("lastName", user.getlName())
				.claim("password", user.getPwd())
				.claim("telephone", user.getTel())
				.claim("title", user.getTitle())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5)) // 5 days
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	public Boolean validateToken(String token, User user) {
		final String username = extractUsername(token);
		return (username.equals(user.getEmail()) && !isTokenExpired(token));
	}
}
