package app.controller;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


import app.model.database.UserEntity;
import app.repository.UserRepository;
import helper.Helper;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthentificationControllerTest {

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


    @Test
    public void connectWithBadPassword_returns_401() throws Exception{

        String body = "{ \"email\": \"itsuki@gmail.com\", \"password\": \"bad-password\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth")
                .contentType("application/json")
                .content(body))
                    .andExpect(status().is(401));
    }


    @SuppressWarnings("null")
    @Test
    public void connectWithGoodPassword_returns_a_token() throws Exception{

        String body = "{ \"email\": \"itsuki@gmail.com\", \"password\": \"green-mechanic\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth")
                .contentType("application/json")
                .content(body))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath("$.token", notNullValue()))
                    .andExpect(jsonPath("$.expireIn", is(3600)))
                    .andExpect(jsonPath("$.authorizationType", is("Bearer Authentication")))
                    .andExpect(jsonPath("$.pseudo", is("Itsuki")));
    }

    
}
