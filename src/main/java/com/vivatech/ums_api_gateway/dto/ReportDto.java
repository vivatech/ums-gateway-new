package com.vivatech.ums_api_gateway.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ReportDto {
    private Map<String, Object> hashData;
    private Integer userId;
}
