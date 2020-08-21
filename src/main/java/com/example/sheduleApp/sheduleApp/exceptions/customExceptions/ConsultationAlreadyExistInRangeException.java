package com.example.sheduleApp.sheduleApp.exceptions.customExceptions;

public class ConsultationAlreadyExistInRangeException extends RuntimeException  {
    public ConsultationAlreadyExistInRangeException(String errorMessage) {
        super(errorMessage);
    }
}
