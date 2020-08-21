package com.example.sheduleApp.sheduleApp;

import com.example.sheduleApp.sheduleApp.domain.requests.AuthenticationRequest;
import com.example.sheduleApp.sheduleApp.repos.ConsultationRepo;
import com.example.sheduleApp.sheduleApp.repos.UserRepo;
import com.example.sheduleApp.sheduleApp.service.ValidateIdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class ConsultationControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ConsultationRepo consultationRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ValidateIdService validateIdService;

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
    public void getConsultations_UsualGetRequest_responseStatus200() throws Exception {
        this.mockMvc
                .perform(
                        get("/consultations")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }
    @Test
    public void getConsultations_UnauthorisedRequest_responseStatus403() throws Exception {
        this.mockMvc
                .perform(
                        get("/consultations")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden());
    }

    @Test
    public void GetConsultations_RequestWithQueryParameterIsReserved_responseStatus200WithOlyOneJsonObject() throws Exception {
        this.mockMvc
                .perform(
                        get("/consultations?reserved=true")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(3)));
    }

    @Test
    public void GetConsultations_RequestWithQueryParameterDate_responseStatus200WithOlyOneJsonObject() throws Exception {
        this.mockMvc
                .perform(
                        get("/consultations?date=2020-08-11")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(4)));
    }

    @Test
    public void GetConsultations_RequestWithQueryParameterTime_responseStatus200WithOlyOneJsonObject() throws Exception {
        this.mockMvc
                .perform(
                        get("/consultations?time=19:00:00")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(3)));
    }

    @Test
    public void getConsultationById_UsualRequest_ResponseStatus200SameId() throws Exception {
        this.mockMvc
                .perform(
                        get("/consultations/3")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isOk());
    }

    @Test
    public void getConsultationById_IdThatDoNotExist_ResponseStatus400() throws Exception {
        this.mockMvc
                .perform(
                        get("/consultations/22")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isBadRequest());
    }

    @Test
    public void createNewConsultation_UsualRequest_OneConsultationObjectAddedToTheDatabase() throws Exception {
        this.mockMvc
                .perform(
                        post("/consultations")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("{\"date\":\"2020-08-16\",\"time\":\"19:00:00\",\"user_who_created_id\" : 2}")
                ).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }
    @Test
    public void createNewConsultation_BadRequest_Status400Response() throws Exception {
        this.mockMvc
                .perform(
                        post("/consultations")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("{\"date\":\"Pipka\",\"time\":\"lol\",\"user_who_created_id\" : 2}")
                ).andExpect(status().isBadRequest());
    }
    @Test
    public void deleteConsultationById_UsualRequest_Status202Response() throws Exception {
        this.mockMvc
                .perform(
                        delete("/consultations/3")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isAccepted());
    }
    @Test
    public void deleteConsultationById_RequestWithBadId_Status400Response() throws Exception {
        this.mockMvc
                .perform(
                        delete("/consultations/33")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isBadRequest());
    }
    @Test
    public void updateConsultation_UsualRequest_ResponseStatus202() throws  Exception {
        this.mockMvc
                .perform(
                        put("/consultations/3")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("{\"date\":\"2020-08-16\",\"time\":\"19:00:00\",\"user_who_created_id\" : 2}")
                ).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }
    @Test
    public void reserveConsultation_NormalRequest_UserWhoReservedWilBeSet() throws Exception {
        this.mockMvc
                .perform(
                        put("/consultations/reserveConsultation/4/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isAccepted());
    }
    @Test
    public void reserveConsultation_BadIdRequest_ResponseStatus400() throws Exception {
        this.mockMvc
                .perform(
                        put("/consultations/reserveConsultation/44/11")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                ).andExpect(status().isBadRequest());
    }
    @Test
    public void createMultipleConsultations_UsualRequest_ResponseStatus202MultipleConsultationsCreated() throws Exception {
        this.mockMvc
                .perform(
                        post("/consultations/createMultiple")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("{\"date\":\"2020-08-19\",\"timeFrom\":\"12:00:00\",\"timeTo\":\"13:00:00\",\"userWhoCreatedId\":\"2\"}")
                ).andExpect(status().isAccepted());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
