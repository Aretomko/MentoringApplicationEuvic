package com.example.sheduleApp.sheduleApp;

import com.example.sheduleApp.sheduleApp.exceptions.customExceptions.ConsultationAlreadyExistInRangeException;
import com.example.sheduleApp.sheduleApp.repos.ConsultationRepo;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import com.example.sheduleApp.sheduleApp.service.CreateMultipleConsultationsService;
import com.example.sheduleApp.sheduleApp.service.ValidateIdService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(value = {"/create-sample-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CreateMultipleConsultationServiceTest {
    @Autowired
    private CreateMultipleConsultationsService createMultipleConsultationsService;
    @Autowired
    private ConsultationRepo consultationRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ValidateIdService validateIdService;

    @Test
    public void checkWeatherPeriodIsFree_DateWithNoConsultations_ReturnTrue(){
        Assert.assertTrue(createMultipleConsultationsService.checkWeatherPeriodIsFree(LocalTime.parse("19:00:00"),
                                                                                      LocalTime.parse("20:00:00"),
                                                                                      LocalDate.parse("2020-08-12")));
    }
    @Test
    public void checkWeatherPeriodIsFree_DateWithConsultationButNotInRange_ReturnsTrue(){
        Assert.assertTrue(createMultipleConsultationsService.checkWeatherPeriodIsFree(LocalTime.parse("19:00:00"),
                LocalTime.parse("20:00:00"),
                LocalDate.parse("2020-08-11")));
    }
    @Test(expected = ConsultationAlreadyExistInRangeException.class)
    public void checkWeatherPeriodIsFree_DateWithConsultationInRange_ReturnsFalse(){
        Assert.assertFalse(createMultipleConsultationsService.checkWeatherPeriodIsFree(LocalTime.parse("11:00:00"),
                LocalTime.parse("13:00:00"),
                LocalDate.parse("2020-08-11")));
    }
    @Test
    public void createConsultationsInRange(){
        createMultipleConsultationsService.createConsultationsInRange(LocalTime.parse("11:00:00"),
                LocalTime.parse("12:00:00"),
                LocalDate.parse("2020-08-18"),
                2);
        Assert.assertEquals(4, consultationRepo.findByDate( LocalDate.parse("2020-08-18")).size());
    }

}
