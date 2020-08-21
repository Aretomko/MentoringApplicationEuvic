package com.example.sheduleApp.sheduleApp.controllers;

import com.example.sheduleApp.sheduleApp.domain.Consultation;
import com.example.sheduleApp.sheduleApp.domain.User;
import com.example.sheduleApp.sheduleApp.domain.requests.CreateMultipleConsultationsRequest;
import com.example.sheduleApp.sheduleApp.exceptions.customExceptions.ImpossibleActionForRoleException;
import com.example.sheduleApp.sheduleApp.repos.ConsultationRepo;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import com.example.sheduleApp.sheduleApp.service.CreateMultipleConsultationsService;
import com.example.sheduleApp.sheduleApp.service.ValidateIdService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("consultations")
public class ConsultationsController {
    private final ConsultationRepo consultationRepo;
    private final UserRepo userRepo;
    private final ValidateIdService validateIdService;
    private final CreateMultipleConsultationsService createMultipleConsultationsService;

    @Autowired
    public ConsultationsController(ConsultationRepo consultationRepo, UserRepo userRepo, ValidateIdService validateIdService, CreateMultipleConsultationsService createMultipleConsultationsService) {
        this.userRepo = userRepo;
        this.consultationRepo = consultationRepo;
        this.validateIdService = validateIdService;
        this.createMultipleConsultationsService = createMultipleConsultationsService;
    }

    @GetMapping
    public List<Consultation> getConsultations(@RequestParam (name="reserved", required = false) Optional<Boolean> isReserved,
                                                  @RequestParam (name="date", required = false) Optional<String> date,
                                                  @RequestParam (name="time", required = false) Optional<String> time) {
        List<Consultation> filteredConsultations = filterConsultations(isReserved, date, time);

        return filteredConsultations;
    }

    @GetMapping("{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable String id){
        Consultation consultationFromDb = null;
        if (validateIdService.validateIdConsultation(id)) {
            consultationFromDb = consultationRepo.findById(Long.valueOf(id)).get();
        }
        return ResponseEntity.ok().body(consultationFromDb);
    }

    @PostMapping
    public ResponseEntity<String> createNewConsultation(@RequestBody String consultationJSON) throws JsonProcessingException {
        Consultation receivedConsultation = parseConsultationObjectFromJson(consultationJSON);
        consultationRepo.save(receivedConsultation);
        return ResponseEntity.accepted().body(consultationJSON);
    }

    @PostMapping("/createMultiple")
    public ResponseEntity<List<Consultation>> createMultipleConsultations(@RequestBody CreateMultipleConsultationsRequest request) throws JsonProcessingException {
        createMultipleConsultationsService.validateCreateMultipleConsultationsRequest(request);

        createMultipleConsultationsService.checkWeatherPeriodIsFree(request.getTimeFrom(),request.getTimeTo(), request.getDate());

        List<Consultation> createdConsultations = createMultipleConsultationsService.createConsultationsInRange(request.getTimeFrom(),request.getTimeTo(),request.getDate(), request.getUserWhoCreatedId());

        return ResponseEntity.accepted().body(createdConsultations);
    }


    @DeleteMapping("{id}")
    public ResponseEntity deleteConsultationById(@PathVariable String id) {
        Consultation consultationToDelete;
        if (validateIdService.validateIdConsultation(id)){
            consultationToDelete = consultationRepo.findById(Long.valueOf(id)).get();
            consultationRepo.delete(consultationToDelete);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateConsultation(@PathVariable String id,
                                                     @RequestBody String consultationJSON) throws JsonProcessingException {
        Consultation receivedConsultation;
        receivedConsultation = parseConsultationObjectFromJson(consultationJSON);
        Consultation consultationFromDb = null;
        if (validateIdService.validateIdConsultation(id)) {
            consultationFromDb = consultationRepo.findById(Long.valueOf(id)).get();
        }
        //coping received consultation object into the object from our database ignoring the id which should be left the same
        BeanUtils.copyProperties(receivedConsultation, consultationFromDb, "id");
        consultationRepo.save(consultationFromDb);
        return ResponseEntity.accepted().body(consultationJSON);
    }



    @PutMapping("/reserveConsultation/{consultationId}/{userId}")
    public ResponseEntity<Consultation> reserveConsultation(@PathVariable String consultationId,
                                                            @PathVariable String userId){
        Consultation consultation = null;
        if (validateIdService.validateIdConsultation(consultationId)) {
            consultation = consultationRepo.findById(Long.valueOf(consultationId)).get();
        }
        User user = null;
        if (validateIdService.validateIdUser(userId)){
            user = userRepo.findById(Long.parseLong(userId)).get();
        }
        consultation.setUserWhoReserved(user);
        consultationRepo.save(consultation);
        return ResponseEntity.accepted().body(consultation);
    }

    private Consultation parseConsultationObjectFromJson(String consultationJSON) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNodeRoot = objectMapper.readTree(consultationJSON);

        JsonNode userWhoCreatedIdJsonNode = jsonNodeRoot.get("user_who_created");
        JsonNode consultationDateJsonNode = jsonNodeRoot.get("date");
        JsonNode consultationTimeJsonNode = jsonNodeRoot.get("time");
        JsonNode userWhoReservedJsonNode = jsonNodeRoot.get("user_who_reserved");

        String userWhoCreatedId = null;
        LocalDate consultationDate = null;
        LocalTime consultationTime = null;
        String userWhoReservedId = null;

        if (userWhoCreatedIdJsonNode != null) userWhoCreatedId = userWhoCreatedIdJsonNode.asText();
        if (consultationDateJsonNode != null) consultationDate = LocalDate.parse(consultationDateJsonNode.asText());
        if (consultationTimeJsonNode != null) consultationTime = LocalTime.parse(consultationTimeJsonNode.asText());
        if (userWhoReservedJsonNode  != null) userWhoReservedId = jsonNodeRoot.get("userWhoReserved").asText();


        User userWhoCreated = null ;
        if(userWhoCreatedId != null && validateIdService.validateIdUser(userWhoCreatedId)) {
            userWhoCreated = userRepo.findById(Long.valueOf(userWhoCreatedId)).get();
            //only user with MASTER role can create consultations
            if (!userWhoCreated.isMaster()) throw new ImpossibleActionForRoleException("only user with MASTER role can create consultations");
        }
        if (userWhoReservedId != null && validateIdService.validateIdUser(userWhoReservedId)){
            User userWhoReserved = userRepo.findById(Long.valueOf(userWhoReservedId)).get();
            //only users with STUDENT Role can reserve consultations
            if(userWhoReserved.isMaster()) throw new ImpossibleActionForRoleException("only users with STUDENT Role can reserve consultations")
                    ;
            return new Consultation(consultationDate, consultationTime, userWhoCreated, userWhoReserved);
        }
        return new Consultation(consultationDate, consultationTime, userWhoCreated);
    }

    private List<Consultation> filterConsultations(Optional<Boolean> isReserved, Optional<String> date, Optional<String> time){
        List<Consultation> consultations = consultationRepo.findAll();
        if (isReserved.isPresent()) {
            if (isReserved.get()) {
                consultations = consultations.stream().filter(i -> i.getUserWhoReserved() != null).collect(Collectors.toList());
            } else {
                consultations = consultations.stream().filter(i -> i.getUserWhoReserved() == null).collect(Collectors.toList());
            }
        }

        if(date.isPresent()){
            LocalDate filterDate = LocalDate.parse(date.get());
            consultations = consultations.stream().filter(i -> i.getDate().equals(filterDate)).collect(Collectors.toList());
        }

        if(time.isPresent()){
            LocalTime filterTime = LocalTime.parse(time.get());
            consultations = consultations.stream().filter(i ->i.getTime().equals(filterTime)).collect(Collectors.toList());
        }

        return consultations;
    }
}
