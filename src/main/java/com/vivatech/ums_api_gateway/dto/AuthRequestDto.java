package com.vivatech.ums_api_gateway.dto;

import com.vivatech.ums_api_gateway.model.Instructors;
import com.vivatech.ums_api_gateway.model.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {
    private String username;
    private String fullname;
    private String password;
    private String token;
    private Date tokenExpiry;
    private String errorMessage;
    private UserRoles roles;
    private Instructors userId;
    private List<SubPrivilegeDto> privilegeList;
    private Integer firstlogin;
    private Integer otp;
    private String result;
    private Integer authId;
}
