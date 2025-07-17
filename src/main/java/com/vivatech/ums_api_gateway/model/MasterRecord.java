package com.vivatech.ums_api_gateway.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "studentrecord")
public class MasterRecord {
  @Id
  @Column(name = "id", nullable = false)
  private Integer id;
  private String lastDepartment;
  private String leavingYear;
  private String lastRegno;
  @JsonSerialize
  @JsonDeserialize
  private String academicRecord;
  @JsonSerialize
  @JsonDeserialize
  private String employmentRecord;

}
