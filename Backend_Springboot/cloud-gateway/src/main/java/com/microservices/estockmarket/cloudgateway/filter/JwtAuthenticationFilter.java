package com.microservices.estockmarket.cloudgateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.microservices.estockmarket.cloudgateway.RouterValidator;
import com.microservices.estockmarket.cloudgateway.jwt.BearerToken;
import com.microservices.estockmarket.cloudgateway.jwt.JwtSupport;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

	@Autowired
	private RouterValidator routerValidator;

	@Autowired
	private JwtSupport jwtSupport;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		ServerHttpRequest request = exchange.getRequest();
		if (routerValidator.isSecured.test(request)) {
			if (this.isAuthMissing(request)) {
				return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
			}

			final BearerToken token = jwtSupport.getBearerfromString(this.getAuthHeader(request));
				System.out.println("Check the bearer : "+token.toString());
			if (jwtSupport.isExpired(token)) {
				return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
			}

			this.populateRequestWithHeaders(exchange, token);
		}
		return chain.filter(exchange);

	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private String getAuthHeader(ServerHttpRequest request) {
		System.out.println("In Here1... "+request.getHeaders().getOrEmpty("Authorization").get(0));
		System.out.println("In Here2... "+request.getHeaders().getOrEmpty("Authorization").get(0).split(" ")[1].trim());
		return request.getHeaders().getOrEmpty("Authorization").get(0).split(" ")[1].trim();
	}

	private boolean isAuthMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey("Authorization");
	}

	private void populateRequestWithHeaders(ServerWebExchange exchange, BearerToken token) {
		System.out.println("DEBUG-1 "+token.toString());
		Claims claims = jwtSupport.getAllClaimsFromToken(token);
		exchange.getRequest().mutate().header("id", String.valueOf(claims.get("id")))
				.header("role", String.valueOf(claims.get("role"))).build();
	}

}
