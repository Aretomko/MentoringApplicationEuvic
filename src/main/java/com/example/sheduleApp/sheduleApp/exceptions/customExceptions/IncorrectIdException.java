package com.example.sheduleApp.sheduleApp.exceptions.customExceptions;

public class IncorrectIdException extends RuntimeException {
    public IncorrectIdException(String errorMessage) {
        super(errorMessage);
    }
}
