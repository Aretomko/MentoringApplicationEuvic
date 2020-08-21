package com.example.sheduleApp.sheduleApp;

import com.example.sheduleApp.sheduleApp.service.PasswordValidationService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PasswordValidationServiceTest {
    @Value("${password.min.length}")
    private int minLength;
    @Value("${password.max.length}")
    private int maxLength;

    @Autowired
    private PasswordValidationService passwordValidationService;

    @Test
    public void validatePassword_ValidPassword_ReturnTrue(){
        Assert.assertTrue(passwordValidationService.validatePassword("1234lol6789"));
    }
    @Test(expected = BadCredentialsException.class)
    public void validatePassword_TooShortPassword_BadCredentialsExceptionThrown(){
        Assert.assertTrue(passwordValidationService.validatePassword("12349"));
    }
    @Test(expected = BadCredentialsException.class)
    public void validatePassword_TooLongPassword_BadCredentialsExceptionThrown(){
        Assert.assertTrue(passwordValidationService.validatePassword("123fdgdgdsgsdgfsdgdsfgfdsgfdsgfsdgdgdfgsd49"));
    }
    @Test(expected = BadCredentialsException.class)
    public void validatePassword_NotAllowedCharacters_BadCredentialsExceptionThrown(){
        Assert.assertTrue(passwordValidationService.validatePassword("12349{}%^&$$"));
    }
}
