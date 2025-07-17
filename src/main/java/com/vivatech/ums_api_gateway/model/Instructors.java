/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vivatech.ums_api_gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author irfan
 * modified by Javed
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "instructors")
public class Instructors {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String code;

  private String name;

  private String staffAddress;

  private Date dob;

  private Date dateOfJoining;

  private String contactnumber;

  private String email;

  private Integer department;

  private String employmenttype;

  private String position;

  private String status;

  private String imagepath;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "user_roles_id")
  private UserRoles userRoles;

  private Integer workingDaysNo;

  private String shifts;

  private String qualification;

  private String reason;

}
