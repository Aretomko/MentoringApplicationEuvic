package com.example.sheduleApp.sheduleApp.exceptions.customExceptions;

public class UserActivationException extends RuntimeException{
    public UserActivationException(String errorMessage) {
        super(errorMessage);
    }
}
