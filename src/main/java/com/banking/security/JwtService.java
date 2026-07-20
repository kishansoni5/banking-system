package com.banking.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	 	@Value("${jwt.secret}")
	    private String secret;

	    @Value("${jwt.expiration}")
	    private long expiration;
	    
	    public String generateToken(UserDetails userDetails){
			return Jwts.builder()
					.subject(userDetails.getUsername())
		            .issuedAt(new Date(System.currentTimeMillis()))
		            .expiration(new Date(System.currentTimeMillis() + expiration))
		            .signWith(getSignInKey())
		            .compact();	    	
	    }

	    public String extractUsername(String token){
	    	 return extractAllClaims(token).getSubject();
	    	
	    }
	    
	    private Claims extractAllClaims(String token) {
	        return Jwts.parser()
	                .verifyWith((SecretKey) getSignInKey())
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	    }

	    private Date extractExpiration(String token) {
	    	return extractAllClaims(token).getExpiration();
	    }
	    
	    private boolean isTokenExpired(String token) {
	    	return extractExpiration(token).before(new Date());
	    }
	    
	    public boolean isTokenValid(String token, UserDetails userDetails) {
	        String username = extractUsername(token);

	        return username.equals(userDetails.getUsername())
	                && !isTokenExpired(token);
	    }  
	    
	    private Key getSignInKey() {
	    	  byte[] keyBytes = Decoders.BASE64.decode(secret);
	    	  return Keys.hmacShaKeyFor(keyBytes);
	    }
	    
	    
	   
}
