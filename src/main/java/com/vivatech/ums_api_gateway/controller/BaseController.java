package com.vivatech.ums_api_gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
public class BaseController {



    protected ResponseEntity<Object> getGETApiResponse(RestTemplate restTemplate, String uri, Class<?> responseType) {
        restTemplate.getInterceptors();
        try {
            Object result = restTemplate.getForObject(uri, Object.class);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            String exceptionBody = e.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            Object errorResult = null;
            try {
                errorResult = objectMapper.readValue(exceptionBody, Object.class);
            } catch (IOException e1) {
                log.error("Exception: ", e1);
            }
            return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
        }

    }
}
