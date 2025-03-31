package com.bank.config;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String jwtString;
	
	@Value("${jwt.expiration}")
	private long jwtExpiry;
	
	@SuppressWarnings("deprecation")
	public String generateToken(Authentication authentication) {
		String userName=authentication.getName();
		Date currentDate=new Date();
		Date expirationDate=new Date(currentDate.getTime()+jwtExpiry);
		
		return Jwts.builder().setSubject(userName)
				.setIssuedAt(currentDate)
				.setExpiration(expirationDate)
				.signWith(key())
				.compact();
	}
	
	private Key key() {
		byte[] bytes=Decoders.BASE64.decode(jwtString);
		return Keys.hmacShaKeyFor(bytes);
	}
	
	public String getUserName(String token) {
		return Jwts.parser().verifyWith((SecretKey)key()).build().parseSignedClaims(token).getPayload().getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token);
			return true;
		} catch (JwtException e) {
			throw new RuntimeException(e);
			
		}catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
			
		}catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}
}
