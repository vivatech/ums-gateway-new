package com.vivatech.ums_api_gateway.login.webfluxlogging;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class CustomWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Log request details
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Method: {} {} Status: {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI(), exchange.getResponse().getStatusCode());
        }));
    }

//    private Mono<String> logRequest(ServerWebExchange exchange) {
//        // Read the request body
//        return DataBufferUtils.join(exchange.getRequest().getBody())
//                .flatMap(dataBuffer -> {
//                    String requestBody = dataBuffer.toString();
//                    log.info("Request: {} {} Body: {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI(), requestBody);
//
//                    // Create a new request with the same body
//                    DataBufferUtils.release(dataBuffer);
//                    return Mono.just(requestBody);
//                });
//    }
//
//    private void logResponse(ServerWebExchange exchange, String requestBody) {
//        // Log response details
//        exchange.getResponse().getHeaders().getContentType();
//        HttpStatus statusCode = exchange.getResponse().getStatusCode();
//        log.info("Response: {} Body: {}", statusCode, requestBody); // Note: You may want to log the actual response body here
//    }
}
