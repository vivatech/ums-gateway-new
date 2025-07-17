package com.vivatech.ums_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
  private String userName;
  private String password;
  private String repassword;
  private String oldpassword;
  private String email;
}
