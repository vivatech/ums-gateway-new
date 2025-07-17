/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vivatech.ums_api_gateway.repository;

import com.vivatech.ums_api_gateway.model.Instructors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author irfan
 */
public interface InstructorsRepository extends JpaRepository<Instructors, Integer> {

  @Query(value = "SELECT a.*, b.name departmentName FROM instructors as a, departments as b where a.department = b.id", nativeQuery = true)
  List<Object[]> getAllStaff();

  @Query(value = "SELECT a.*, b.name departmentName FROM instructors as a, departments as b where a.department = b.id and a.code=?1", nativeQuery = true)
  List<Object[]> getOneStaff(String code);

  @Query(value = "Select name from instructors where code =?1", nativeQuery = true)
  String findByName(String code);

  @Query(value = "Select department from instructors where code =?1", nativeQuery = true)
  Integer findByDepartmentId(String code);

  Instructors findByCode(String code);

  List<Instructors> findByDepartmentIn(List<Integer> departmentIdList);

  Instructors findByNameContaining(String name);

  List<Instructors> findByStatusOrderByDateOfJoiningDesc(String status);

  Instructors findByContactnumber(String contactNo);

  Instructors findByEmail(String email);

  List<Instructors> findTop10ByCodeContaining(String staffCode);
}
