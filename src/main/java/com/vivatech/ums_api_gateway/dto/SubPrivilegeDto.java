package com.vivatech.ums_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubPrivilegeDto {
    private Integer id;
    private String name;
    private String displayName;
    private Integer subPrivilegeId;
}
