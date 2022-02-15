package com.microservices.estockmarket.cloudgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import com.microservices.estockmarket.cloudgateway.jwt.JwtAuthenticationManager;
import com.microservices.estockmarket.cloudgateway.jwt.JwtServerAuthenticationConverter;

import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtServerAuthenticationConverter converter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails user = User.builder()
                .username("batman")
                .password(encoder.encode("batman"))
                .roles("USER", "ADMIN", "DEVELOPER")
                .build();
        UserDetails user1 = User.builder()
                .username("satya")
                .password(encoder.encode("satya"))
                .roles("USER", "ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user,user1);
    }
	
	 @Bean
	 public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtAuthenticationManager authManager) {
		 AuthenticationWebFilter filter = new AuthenticationWebFilter(authManager);
		 
		filter.setServerAuthenticationConverter(converter);
		
		http
			.exceptionHandling()
			.authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
				swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				swe.getResponse().getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
			}))
			.accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
				System.out.println("INNNNNNN...");
				swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
			}))
			.and()
			.authorizeExchange()
			.pathMatchers(HttpMethod.POST, "/login").permitAll()
			.anyExchange()
			.authenticated()
			.and()
			.addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
				.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable();

		return http.build();
	  }

}
