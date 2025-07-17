package com.vivatech.ums_api_gateway.repository;



import com.vivatech.ums_api_gateway.model.NewUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewUsersRepository extends JpaRepository<NewUsers, Integer> {
  NewUsers findByUserName(String username);

  NewUsers findByUserId(Integer id);

  List<NewUsers> findByStatus(String status);

  List<NewUsers> findByUserNameAndStatus(String userName, String blocked);
}
