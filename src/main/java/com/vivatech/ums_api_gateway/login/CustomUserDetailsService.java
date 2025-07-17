package com.vivatech.ums_api_gateway.login;


import com.vivatech.ums_api_gateway.model.NewUsers;
import com.vivatech.ums_api_gateway.model.UserRoles;
import com.vivatech.ums_api_gateway.repository.NewUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private NewUsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NewUsers users = usersRepository.findByUserName(username);
        if (users != null){
          return new User(users.getUserName(), users.getPassword(), new ArrayList<>());
        }
        return null;
    }

    public UserDetails loadUserByApiKey(String apiKey) throws UsernameNotFoundException {
        if (apiKey.equalsIgnoreCase("123456")){
            return new User("Admas Biller", "123456", new ArrayList<>());
        }
        return null;
    }

    public UserRoles findRolesByUsername(String username) {
        NewUsers byUserName = usersRepository.findByUserName(username);
        UserRoles roles = byUserName.getUserRoles();
        return roles;
    }

}
