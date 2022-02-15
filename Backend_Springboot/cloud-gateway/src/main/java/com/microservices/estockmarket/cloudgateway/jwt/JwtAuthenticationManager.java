package com.microservices.estockmarket.cloudgateway.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
	
	@Autowired
	private JwtSupport jwtSupport;
	
	@Autowired
	private ReactiveUserDetailsService users;
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		System.out.println("KISHORE-1");
		return Mono.justOrEmpty(authentication)
				.filter(auth -> auth instanceof BearerToken)
				.cast(BearerToken.class)
				.flatMap(jwt -> Mono.just(validate(jwt)))
				.onErrorMap(error -> new InvalidBearerToken(error.getMessage()));
	}
	
	
	private Authentication validate(BearerToken token) {
		System.out.println("KISHORE-2");
		String username = jwtSupport.getUserName(token);
		Mono<UserDetails> userDetails = users.findByUsername(username);
		System.out.println("SATYAAA11");
		if(jwtSupport.isValid(token, userDetails.block())) {
			System.out.println("SATYAAA22");
			return new UsernamePasswordAuthenticationToken(token.getPrincipal(),token.getCredentials(),token.getAuthorities());
		}
		System.out.println("SATYAAA33");
		 throw new IllegalArgumentException("Token is not valid.");
	}
}
