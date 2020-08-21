package com.example.sheduleApp.sheduleApp.service;

import com.example.sheduleApp.sheduleApp.exceptions.customExceptions.IncorrectIdException;
import com.example.sheduleApp.sheduleApp.repos.ConsultationRepo;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.text.ParseException;

@Service
public class ValidateIdService {
    private final ConsultationRepo consultationRepo;
    private final UserRepo userRepo;

    public ValidateIdService(ConsultationRepo consultationRepo, UserRepo userRepo){
        this.consultationRepo = consultationRepo;
        this.userRepo = userRepo;
    }

    public boolean validateIdConsultation(String id){
        try {
            if (consultationRepo.findById(Long.valueOf(id)).isPresent()){
                return true;
            }else {
                throw new IncorrectIdException("Object with such id is not present in the db");
            }
        }
        catch (MethodArgumentTypeMismatchException | NullPointerException | NumberFormatException ex){
            throw new IncorrectIdException("Incorrect id type");
        }
    }
    public boolean validateIdUser(String id){
        try {
            if (userRepo.findById(Long.valueOf(id)).isPresent()){
                return true;
            }else {
                throw new IncorrectIdException("User object with such id is not present in the db");
            }
        }
        catch (MethodArgumentTypeMismatchException | NullPointerException | NumberFormatException ex){
            throw new IncorrectIdException("Incorrect User id");
        }
    }
}
