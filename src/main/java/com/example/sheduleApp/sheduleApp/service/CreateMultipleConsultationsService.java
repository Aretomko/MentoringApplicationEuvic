package com.example.sheduleApp.sheduleApp.service;

import com.example.sheduleApp.sheduleApp.domain.Consultation;
import com.example.sheduleApp.sheduleApp.domain.User;
import com.example.sheduleApp.sheduleApp.domain.requests.CreateMultipleConsultationsRequest;
import com.example.sheduleApp.sheduleApp.exceptions.customExceptions.ConsultationAlreadyExistInRangeException;
import com.example.sheduleApp.sheduleApp.repos.ConsultationRepo;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateMultipleConsultationsService {
    private final ConsultationRepo consultationRepo;
    private final UserRepo userRepo;
    private final ValidateIdService validateIdService;

    public CreateMultipleConsultationsService(ConsultationRepo consultationRepo, UserRepo userRepo, ValidateIdService validateIdService) {
        this.consultationRepo = consultationRepo;
        this.userRepo = userRepo;
        this.validateIdService = validateIdService;
    }
    public void validateCreateMultipleConsultationsRequest(CreateMultipleConsultationsRequest request){
        if (request.getTimeFrom().isAfter(request.getTimeTo())){
            throw new RuntimeException("Impossible condition when begin time is after finish time");
        }
        validateIdService.validateIdUser(String.valueOf(request.getUserWhoCreatedId()));
    }

    public boolean checkWeatherPeriodIsFree(LocalTime from, LocalTime to, LocalDate date){
        List<Consultation> consultationsWithTheSameDate =  consultationRepo.findByDate(date);
        for (Consultation c : consultationsWithTheSameDate){
            LocalTime consultationTime = c.getTime();
            if (consultationTime.isAfter(from) && consultationTime.isBefore(to)) {
                throw new ConsultationAlreadyExistInRangeException("Consultation already created on " + consultationsWithTheSameDate.get(0).getTime().toString());
            }
        }
        return true;
    }
    public List<Consultation> createConsultationsInRange(LocalTime from, LocalTime to, LocalDate date, int userWhoCreatedId){
        List<Consultation> createdConsultations = new ArrayList<>();
        User userWhoCreated = userRepo.findById((long) userWhoCreatedId).get();
        while (from.isBefore(to)) {
            Consultation newConsultation = new Consultation(date, from, userWhoCreated);
            consultationRepo.save(newConsultation);
            createdConsultations.add(newConsultation);
            from = from.plus(15, ChronoUnit.MINUTES);
        }
        return createdConsultations;
    }
}
