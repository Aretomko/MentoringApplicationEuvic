package com.example.sheduleApp.sheduleApp;

import com.example.sheduleApp.sheduleApp.domain.requests.AuthenticationRequest;
import com.example.sheduleApp.sheduleApp.repos.ConsultationRepo;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import com.example.sheduleApp.sheduleApp.service.MailSender;
import com.example.sheduleApp.sheduleApp.service.PasswordValidationService;
import com.example.sheduleApp.sheduleApp.service.UserService;
import com.example.sheduleApp.sheduleApp.service.ValidateIdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@Sql(value = {"/create-sample-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordValidationService passwordValidationService;
    @Autowired
    private ValidateIdService validateIdService;
    @Autowired
    private ConsultationRepo consultationRepo;

    private String token;

    @Before
    public void setup() {
        token = given()
                .contentType(ContentType.JSON)
                .body(new AuthenticationRequest("lolAdmin", "123"))
                .when().post("http://localhost:8080/auth/signin")
                .andReturn().jsonPath().getString("token");
    }


    @Test
    public void getUsers_NormalRequestWithoutParameters_ReturningListOfAllUsers() throws Exception {
        this.mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }
    @Test
    public void getUsers_RequestAllUsersWithMASTER_ReturningAllStudentsWithMASTERRole() throws Exception {
        this.mockMvc.perform(get("/users?role=MASTER")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }
    @Test
    public void getUsers_RequestAllUsersWithRoleWhichDoNotExist_ReturnStatus400() throws Exception {
        this.mockMvc.perform(get("/users?role=MASTEROK")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isBadRequest());
    }
    @Test
    public void getUsers_RequestAllUsersWithProvidedUsername_ReturningAllStudentsWithProvidedUsername() throws Exception {
        this.mockMvc.perform(get("/users?username=lol")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("lol")));
    }
    @Test
    public void getUsers_RequestAllUsersWithUsernameWhichDoNotExist_ReturnStatus400() throws Exception {
        this.mockMvc.perform(get("/users?username=lolitaBabuska")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }
    @Test
    public void getUsers_RequestAllUsersWithProvidedEmail_ReturningAllStudentsWithProvidedEmail() throws Exception {
        this.mockMvc.perform(get("/users?email=aroma7752424@gmial.com")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0].email", is("aroma7752424@gmial.com")));
    }
    @Test
    public void createUserTest_UsualRequest_UserIsCreated() throws Exception {
        this.mockMvc.perform(post("/users/registration")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newUser\",\"password\":\"123456789\",\"email\" : \"aroma7752424@gmail.com\"}")
        ).andExpect(status().isAccepted());
        this.mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4)));
    }
    @Test
    public void activateUser_UsualRequest_UserActivated() throws Exception {
        this.mockMvc.perform(post("/users/registration")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newUser\",\"password\":\"123456789\",\"email\" : \"aroma7752424@gmail.com\"}")
        );
        this.mockMvc.perform(post("/users/activate")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isAccepted());
    }
    @Test
    public void deleteUser_UsualRequest_UserWillBeDeleted() throws Exception {
        this.mockMvc.perform(delete("/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isAccepted());
    }
}
