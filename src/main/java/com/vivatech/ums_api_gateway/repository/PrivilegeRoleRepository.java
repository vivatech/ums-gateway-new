package com.vivatech.ums_api_gateway.repository;


import com.vivatech.ums_api_gateway.model.PrivilegeRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PrivilegeRoleRepository extends JpaRepository<PrivilegeRoles, Integer> {
    List<PrivilegeRoles> findByRoleId(Integer roleId);

    @Transactional
    @Modifying
    void deleteByRoleId(Integer roleId);

}
