package com.vivatech.ums_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  private Integer userId;
  private String name;
  private String userType;
  private Integer teacherCode;
  private String code;
  private String email;

}
