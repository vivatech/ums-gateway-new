package com.vivatech.ums_api_gateway.login;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException exception) {

		System.out.println("Authentication Entry point: " + exchange.getResponse().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

		return Mono.fromRunnable(() -> {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		});
	}
}
