package com.microservices.estockmarket.apigateway.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		
		http.cors().configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
				CorsConfiguration config= new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("*"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true);
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setMaxAge(3600L);
				return config;
			}
		}).and()
		.csrf().disable().authorizeExchange().anyExchange().authenticated().and().oauth2Login();
		return http.build();
	}	
}
