package com.vivatech.ums_api_gateway.helper;

import org.springframework.web.server.ServerWebExchange;

public class Constants {

    public static final String DEFAULT_MDC_UUID_TOKEN_KEY = "traceid";
    public static final String DEFAULT_MDC_USER_KEY = "user";
    public static final String TRACE_ID_HEADER_NAME = "x-trace-id";
    public static final String CLIENT_IP_HEADER_NAME = "X-Forwarded-For";
    public static final String USER_ID_HEADER_NAME = "x-user-id";
    public static final String CLAIM_ROLE = "role";


    public static String getClientIP(ServerWebExchange request) {
        String xfHeader = request.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRequest().getRemoteAddress().getHostString();
        }
        return xfHeader.split(",")[0];
    }
}
