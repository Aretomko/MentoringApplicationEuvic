package com.example.sheduleApp.sheduleApp.config.security;

import com.example.sheduleApp.sheduleApp.domain.Role;
import com.example.sheduleApp.sheduleApp.domain.User;
import com.example.sheduleApp.sheduleApp.exceptions.customExceptions.UserIsNotActivatedException;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepo userRepo;

    public CustomAuthenticationProvider(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";

        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("User with such username does not exist");
        }
        String  userPasswordTest = user.getPassword();
        if (!userPasswordTest.equals(password)) {
            throw new BadCredentialsException("Incorrect password");
        }
        if (!user.isActive()){
            throw new UserIsNotActivatedException("User is not activated");
        }
        Set<Role> userRights = user.getRoles();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password, userRights);
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}