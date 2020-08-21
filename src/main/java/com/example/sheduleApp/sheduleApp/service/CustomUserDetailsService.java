package com.example.sheduleApp.sheduleApp.service;

import com.example.sheduleApp.sheduleApp.domain.User;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo users;

    public CustomUserDetailsService(UserRepo users) {
        this.users = users;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = this.users.findByUsername(username);
        if (foundUser == null) throw new UsernameNotFoundException("Username: " + username + " not found")
                ;
        return foundUser;
    }
}
