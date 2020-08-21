package com.example.sheduleApp.sheduleApp;

import com.example.sheduleApp.sheduleApp.exceptions.customExceptions.IncorrectIdException;
import com.example.sheduleApp.sheduleApp.repos.ConsultationRepo;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import com.example.sheduleApp.sheduleApp.service.ValidateIdService;
import org.junit.Assert;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(value = {"/create-sample-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ValidateIdServiceTest {
    @Mock
    private ConsultationRepo consultationRepo;
    @Mock
    private UserRepo userRepo;
    @Autowired
    private ValidateIdService validateIdService;


    @Test
    public void validateIdConsultation_CorrectId_returnTrue(){
        Assert.assertEquals(true, validateIdService.validateIdConsultation("4"));
    }
    @Test(expected = IncorrectIdException.class)
    public void validateIdConsultation_notExistedId_ThrowIncorrectIdException(){
        validateIdService.validateIdConsultation("22");
    }
    @Test(expected = IncorrectIdException.class)
    public void validateIdConsultation_notIntegerId_ThrowIncorrectIdException(){
       validateIdService.validateIdConsultation("12323r234");
    }
    @Test
    public void validateIdUser_CorrectId_returnTrue(){
        Assert.assertEquals(true, validateIdService.validateIdUser("1"));
    }
    @Test(expected = IncorrectIdException.class)
    public void validateIdUser_notIntegerId_returnTrue(){
        validateIdService.validateIdUser("bfgbbgffd");
    }
    @Test(expected = IncorrectIdException.class)
    public void validateIdUser_notExistingId_ThrowIncorrectIdException(){
        validateIdService.validateIdUser("34");
    }

}
