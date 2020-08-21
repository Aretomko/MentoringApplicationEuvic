package com.example.sheduleApp.sheduleApp.exceptions.customExceptions;

public class UserIsNotActivatedException extends RuntimeException{
    public UserIsNotActivatedException(String errorMessage) {
        super(errorMessage);
    }
}
