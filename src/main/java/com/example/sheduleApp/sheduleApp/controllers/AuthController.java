package com.example.sheduleApp.sheduleApp.controllers;

import com.example.sheduleApp.sheduleApp.config.security.CustomAuthenticationProvider;
import com.example.sheduleApp.sheduleApp.config.security.JwtTokenProvider;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sheduleApp.sheduleApp.domain.requests.AuthenticationRequest;
import java.util.HashMap;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    CustomAuthenticationProvider authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepo users;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
        try {
            String username = data.getUsername();
            UsernamePasswordAuthenticationToken tokenObject = new UsernamePasswordAuthenticationToken(username, data.getPassword());
            authenticationManager.authenticate(tokenObject);
            String token = jwtTokenProvider.createToken(username, this.users.findByUsername(username).getStringRoles());
            HashMap<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            logger.info("User" + username + "has logged into the application");
            return ok(model);
        } catch (AuthenticationException e)  {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

}
