package com.example.sheduleApp.sheduleApp.exceptions.customExceptions;

public class RoleParsingException extends RuntimeException {
    public RoleParsingException(String errorMessage) {
        super(errorMessage);
    }
}
