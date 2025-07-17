package com.vivatech.ums_api_gateway.login;

import com.vivatech.ums_api_gateway.helper.UMSEnums;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component("permissionService")
public class PermissionService {

    public boolean check(String... permission) {
        return authorizeByPermission(permission);
    }

    private boolean authorizeByPermission(String... attrValue) {
        Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> allowedRoles = Arrays.asList(attrValue);

        for (GrantedAuthority role : roles) {
            String roleString = role.getAuthority();

            if (roleString.equals(UMSEnums.Roles.ADMIN.toString())) {
                return true;
            }

            if (allowedRoles.contains(roleString)) {
                return true;
            }
        }
        return false;
    }

}