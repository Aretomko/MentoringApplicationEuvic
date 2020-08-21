package com.example.sheduleApp.sheduleApp.controllers;

import com.example.sheduleApp.sheduleApp.domain.Consultation;
import com.example.sheduleApp.sheduleApp.domain.Role;
import com.example.sheduleApp.sheduleApp.domain.User;
import com.example.sheduleApp.sheduleApp.domain.requests.RegistrationRequest;
import com.example.sheduleApp.sheduleApp.repos.ConsultationRepo;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import com.example.sheduleApp.sheduleApp.service.MailSender;
import com.example.sheduleApp.sheduleApp.service.PasswordValidationService;
import com.example.sheduleApp.sheduleApp.service.UserService;
import com.example.sheduleApp.sheduleApp.service.ValidateIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserRepo userRepo;

    private final MailSender mailSender;

    private final UserService userService;

    private final PasswordValidationService passwordValidationService;

    private final ValidateIdService validateIdService;

    private final ConsultationRepo consultationRepo;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserRepo userRepo,
                          MailSender mailSender,
                          UserService userService,
                          PasswordValidationService passwordValidationService,
                          ValidateIdService validateIdService,
                          ConsultationRepo consultationRepo) {
        this.consultationRepo = consultationRepo;
        this.validateIdService = validateIdService;
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.userService = userService;
        this.passwordValidationService = passwordValidationService;
    }
    @GetMapping
    public List<User> getUsers(@RequestParam(name = "role", required = false) Optional<String> role,
                               @RequestParam(name = "username", required = false) Optional<String> username,
                               @RequestParam(name = "reserved", required = false) Optional<String> email) {

        List<User> filteredUsers = filterUsers(role, username, email);

        return filteredUsers;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> createUser(@RequestBody RegistrationRequest request) {
        if (userRepo.findByUsername(request.getUsername()) != null)
            throw new BadCredentialsException("Username already exist");

        passwordValidationService.validatePassword(request.getPassword());

        User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail());

        userService.addUser(newUser);

        logger.info("User " + newUser.getUsername() + "was registered");

        return new ResponseEntity<>(newUser, new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<String> activateUser(@PathVariable String code) {
        userService.activateUser(code);

        logger.info("User " + userRepo.findByActivationCode(code).getUsername() + "was successfully activated");

        return new ResponseEntity<>("User was successful activated", new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        User user = null;
        if (validateIdService.validateIdUser(id)) {
            user = userRepo.findById(Long.parseLong(id)).get();
        }
        List<Consultation> consultationsToDelete = new ArrayList<>();
        if (user.isMaster()){
            consultationsToDelete.addAll(consultationRepo.findByUserWhoCreated(user));
        }else{
            consultationsToDelete.addAll(consultationRepo.findByUserWhoReserved(user));
        }
        consultationRepo.deleteAll(consultationsToDelete);
        userRepo.delete(user);
        logger.info("User " + user.getUsername() + " was deleted");
        return new ResponseEntity<User>(user, new HttpHeaders(), HttpStatus.ACCEPTED);
    }

    private List<User> filterUsers(Optional<String> stringRole, Optional<String> username, Optional<String> email) {
        List<User> users = userRepo.findAll();

        if (stringRole.isPresent()) {
            final Role role = Role.parseRoleFromString(stringRole.get());
            users = users.stream().filter(i -> i.getRoles().contains(role)).collect(Collectors.toList());
        }
        if (username.isPresent()) {
            users = users.stream().filter(i -> i.getUsername().equals(username.get())).collect(Collectors.toList());
        }
        if (email.isPresent()) {
            users = users.stream().filter(i -> i.getEmail().equals(email.get())).collect(Collectors.toList());
        }
        return users;
    }

}


