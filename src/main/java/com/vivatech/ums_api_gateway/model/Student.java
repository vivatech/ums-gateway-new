package com.vivatech.ums_api_gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  @Column(name = "firstname")
  private String firstName;
  @Column(name = "lastname")
  private String lastName;
  @Column(name = "fathername")
  private String fatherName;
  private String gender;
  private String dob;
  @Column(name = "lastschoolname")
  private String lastSchoolName;
  @Column(name = "lastschooladdress")
  private String lastSchoolAddress;
  @Column(name = "lastschoolpassingyear")
  private String lastSchoolPassingYear;
  @Column(name = "lastschoolrollno")
  private String lastSchoolRollNo;
  @Column(name = "lastschoolcertificateno")
  private String lastSchoolCertificateNo;
  @Column(name = "lastschoolgrade")
  private String lastSchoolGrade;
  @Column(name = "contactno")
  private String contactNo;
  private String email;
  @Column(name = "guardianname")
  private String guardianName;
  @Column(name = "guardiancontactno")
  private String guardianContactNo;
  @Column(name = "firstfacultyid")
  private Integer firstFacultyId;
  @Column(name = "firstdepartmentid")
  private Integer firstDepartmentId;
  @Column(name = "secondfacultyid")
  private Integer secondFacultyId;
  @Column(name = "seconddepartmentid")
  private Integer secondDepartmentId;
  @Column(name = "allottedfaculty")
  private Integer allottedFaculty;
  @Column(name = "allotteddepartment")
  private Integer allottedDepartment;
  @Column(name = "shiftid")
  private Integer shiftId;
  @Column(name = "studenttype")
  private String studentType;
  @Column(name = "receiptno")
  private String receiptNo;
  @Column(name = "amount")
  private Double amount;
  @Column(name = "employmenttype")
  private String employmentType;
  @Column(name = "imagepath")
  private String imagePath;
  @Column(name = "status")
  private String status;
  @Column(name = "createddate")
  private LocalDateTime createdDate;
  @Column(name = "modifieddate")
  private LocalDateTime modifiedDate;
  @Column(name = "registrationno")
  private String registrationNo;
  @Column(name = "admissionno")
  private String admissionNo;
  @Column(name = "sectionid")
  private Integer sectionId;
  @Column(name = "batchid")
  private Integer batchId;
  private Integer semesterid;
  @Column(name = "registrationremarks")
  private String registrationRemarks;
  @Column(name = "scholarshipname")
  private String scholarshipName;
  @Column(name = "scholarshippercent")
  private Integer scholarshipPercent;
  @Column(name = "sponsorname")
  private String sponsorName;
  @Column(name = "sponsorcontact")
  private String sponsorContact;
  @Column(name = "lastschoolstate")
  private String lastSchoolState;
  @Column(name = "lastschoolcountry")
  private String lastSchoolCountry;
  @Column(name = "programType")
  private String programType;
  @Column(name = "transferInstitution")
  private String transferInstitution;
  @Column(name = "remarks")
  private String remarks;
  @Column(name = "certificateRemark")
  private String certificateRemark;

  @Column(name = "academicYear")
  private String academicYear;


  @JsonIgnore
  @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
  private List<MasterRecord> masterRecordList = new ArrayList<>();


  public Integer getSemesterid() {
    return semesterid;
  }

  public void setSemesterid(Integer semesterid) {
    this.semesterid = semesterid;
  }
}
