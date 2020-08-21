package com.example.sheduleApp.sheduleApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class PasswordValidationService {
    @Value("${password.min.length}")
    private int minLength;
    @Value("${password.max.length}")
    private int maxLength;

    public boolean validatePassword(String password){
        if (password.length() > maxLength || password.length() < minLength){
            throw new BadCredentialsException("Password should be shorter than 30 and longer than 8 characters");
        }
        if (!password.matches("^[a-zA-Z0-9]+$")){
            throw new BadCredentialsException("Password should contain at least one letter and at least one character");
        }
        return true;
    }
}
