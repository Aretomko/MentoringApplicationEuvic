package com.example.sheduleApp.sheduleApp.domain;//package com.example.sheduleApp.sheduleApp.domain;

import com.example.sheduleApp.sheduleApp.exceptions.customExceptions.RoleParsingException;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT, MASTER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

   public static Role parseRoleFromString(String role){
        switch (role){
            case("MASTER") : return Role.MASTER;
            case("STUDENT") : return Role.STUDENT;
            case("ADMIN") : return Role.ADMIN;
            default: throw new RoleParsingException("Can't parse user's role");
        }
    }
}
