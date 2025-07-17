package com.vivatech.ums_api_gateway.repository;


import com.vivatech.ums_api_gateway.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRolesRepository extends JpaRepository<UserRoles, Integer> {
  UserRoles findByName(String roleName);

  List<UserRoles> findByNameContaining(String roleName);
}
