package com.vivatech.ums_api_gateway.repository;


import com.vivatech.ums_api_gateway.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    @Query("select count(p) from Privilege p")
    long privilegeCount();

    @Query(value = "select * from privilege where name =?1", nativeQuery = true)
    Privilege findByPrivilegeName(String privilege);

    List<Privilege> findByNameIn(Set<String> keySet);

    Privilege findTopByOrderByIdDesc();
}
