package com.vivatech.ums_api_gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@SpringBootApplication
//@PropertySource("classpath:application.properties")
@PropertySource("file:////home/core/ums/gateway/application.properties")
public class UmsApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(UmsApiGatewayApplication.class, args);
	}

//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("ums_hrpm_app_blob", r -> r
//						.path("/**")
//						.filters(f -> f
//								.modifyResponseBody(Object.class, Object.class, (exchange, body) -> {
//									log.info("Route triggered for request: {}", exchange.getRequest().getURI());
//									if (body != null && body instanceof byte[]) {
//										log.info("The body is a byte array");
//										return Mono.just(Base64Utils.encodeToString((byte[]) body));
//									}
//									log.info("The body is not a byte array");
//									return body != null ? Mono.just(body.toString()) : Mono.empty();
//								}))
//						.uri("http://139.84.167.86:7070/hrpm/"))
//				.build();
//	}




}
