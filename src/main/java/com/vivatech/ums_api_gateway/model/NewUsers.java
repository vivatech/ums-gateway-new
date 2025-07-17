package com.vivatech.ums_api_gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_tbl")
public class NewUsers {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Integer id;
  private String userName;
  private String password;
  private String email;
  private Integer userId;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "user_roles_id")
  private UserRoles userRoles;

  private String status;

  private Integer loginAttempt;

  private Date blockedTime;

  private Integer firstlogin;

  private Integer otp;

}
