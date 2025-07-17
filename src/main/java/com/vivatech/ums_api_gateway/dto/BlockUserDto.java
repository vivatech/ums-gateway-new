package com.vivatech.ums_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockUserDto {

    private Integer id;
    private String userId;
    private String userName;
    private Integer userContactNo;
    private String userType;
}
