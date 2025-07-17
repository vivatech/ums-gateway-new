package com.vivatech.ums_api_gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackMethodController {

    @GetMapping("/user-fallback")
    public String umsServiceFallback(){
        return "UMS Service is down";
    }

    @GetMapping("/tracker-service-fallback")
    public String trackerServiceFallback(){
        return "Tracker Service is down";
    }

    @GetMapping("/auth-fallback")
    public String authFallback(){
        return "Authentication Service is down";
    }
}
