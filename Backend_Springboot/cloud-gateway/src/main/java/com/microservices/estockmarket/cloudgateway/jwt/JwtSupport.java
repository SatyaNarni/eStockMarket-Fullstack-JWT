package com.microservices.estockmarket.cloudgateway.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtSupport {
	
	/* 
	 * To choose one key 
	 IntStream.rangeClosed(0, 10).forEach((i) -> {
			System.out.println(Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()));
		});

	 */
	 private SecretKey  key = Keys.hmacShaKeyFor("tgiUJptWXV3geTNKeQcRlq+Q31umg3Dvvcv31Fco8ms=".getBytes());
	 
	 JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
	 
	public BearerToken generate(String username) {
		JwtBuilder  builder = Jwts.builder()
								.setSubject(username)
								.setIssuedAt(Date.from(Instant.now()))
								.setExpiration(Date.from(Instant.now().plus(20, ChronoUnit.MINUTES)))
								.signWith(key);
		return new BearerToken(AuthorityUtils.NO_AUTHORITIES,builder.compact());
	}
	
	public BearerToken getBearerfromString(String tokenValue) {
		return new BearerToken(AuthorityUtils.NO_AUTHORITIES,tokenValue);
	}
	
	public String  getUserName(BearerToken token) {
		return parser.parseClaimsJws(token.getValue()).getBody().getSubject();
	}
	
	 public Claims getAllClaimsFromToken(BearerToken token) {
	    return parser.parseClaimsJws(token.getValue()).getBody();
	 }

	public boolean isValid(BearerToken token, UserDetails user) {	
		Claims claims = this.getAllClaimsFromToken(token);
		System.out.println("SATYAAA33-1 "+!this.isExpired(token)+" "+(claims.getSubject().equals(user.getUsername())));
		return (!this.isExpired(token) && (claims.getSubject().equals(user.getUsername())));
	}
	
	public boolean isExpired(BearerToken token) {	
		return !this.getAllClaimsFromToken(token).getExpiration().after(Date.from(Instant.now()));
	}
}
