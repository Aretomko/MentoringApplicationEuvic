package com.example.sheduleApp.sheduleApp.exceptions.customExceptions;

public class InvalidJwtAuthenticationException extends RuntimeException{
    public InvalidJwtAuthenticationException(String errorMessage) {
        super(errorMessage);
    }

}
