package app.controller;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.model.database.UserEntity;
import app.repository.UserRepository;
import helper.Helper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class InscriptionControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    
	/** A method to initialize the database before each test. */
	@BeforeEach
	public void init() throws NoSuchAlgorithmException{

        String password = "green-mechanic";
        String verification_link = "https://mynrista-api/check-mail/24de8968a01e4e39";
        String hash = Helper.hash(password);
        UserEntity user = new UserEntity("Itsuki", "itsuki@gmail.com", hash, verification_link, true);
		
		this.userRepository.deleteAll();
		this.userRepository.save(user);
		
	}


    @SuppressWarnings("null")
    @Test
    public void usersValidity_detect_existing_users() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .get("/users/validity/Itsuki/shadow@gmail.com"))
                    .andExpect(status().is(200))
				    .andExpect(jsonPath("$.isRegistrationAccepted", is(false)))
                    .andExpect(jsonPath("$.pseudoAlreadyExisting", is(true)))
                    .andExpect(jsonPath("$.emailAlreadyExisting", is(false)))
                    .andExpect(jsonPath("$.othersRules[0]", is("The passwords must be of length >= 6")))
                    .andExpect(jsonPath("$.othersRules[1]", is("The fields pseudo, email, and password are mandatories")));
    }


    @SuppressWarnings("null")
    @Test
    public void usersValidity_accept_new_users() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .get("/users/validity/Wolf/wolf@gmail.com"))
                    .andExpect(status().is(200))
				    .andExpect(jsonPath("$.isRegistrationAccepted", is(true)))
                    .andExpect(jsonPath("$.pseudoAlreadyExisting", is(false)))
                    .andExpect(jsonPath("$.emailAlreadyExisting", is(false)))
                    .andExpect(jsonPath("$.othersRules[0]", is("The passwords must be of length >= 6")))
                    .andExpect(jsonPath("$.othersRules[1]", is("The fields pseudo, email, and password are mandatories")));
    }



    @Test
    public void createAndVerifyUser_must_succeed() throws Exception{

        String body = "{ \"pseudo\": \"Tora\", \"email\": \"tora.lockfire@gmail.com\"," + 
                        "  \"password\": \"green-mechanic\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType("application/json")
                .content(body))
                    .andExpect(status().is(201))
                    .andReturn();

        String verificationLink = result.getResponse().getHeader("VERIFICATION_LINK");
        assertNotNull(verificationLink);
        assertTrue(verificationLink.contains("/check-mail/"));
        String[] parts = verificationLink.split("/check-mail/");
        String shortVerificationLink = "/check-mail/" + parts[1];

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders
                .get(shortVerificationLink))
                    .andExpect(status().is(200))
                    .andReturn();

        String htmlGenerated = result2.getResponse().getContentAsString();

        assertTrue(htmlGenerated.contains("a été validée avec succès."));
    }



}
