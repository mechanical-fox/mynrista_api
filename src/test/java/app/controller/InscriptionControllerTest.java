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
        String hash = Helper.hash(password);
        UserEntity user = new UserEntity("Itsuki", "itsuki@gmail.com", hash);
		
		this.userRepository.deleteAll();
		this.userRepository.save(user);
		
	}


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
    public void createUser_succeed_when_performed_correctly() throws Exception{

        String body = "{ \"pseudo\": \"Tora\", \"email\": \"tora.lockfire@gmail.com\"," + 
                        "  \"password\": \"green-mechanic\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType("application/json")
                .content(body))
                    .andExpect(status().is(201));
    }

    @Test
    public void createUser_returns_400_when_pseudo_isnt_filled() throws Exception{

        String body = "{ \"pseudo\": null, \"email\": \"tora.lockfire@gmail.com\"," + 
                        "  \"password\": \"green-mechanic\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType("application/json")
                .content(body))
                    .andExpect(status().is(400));

    }

    @Test
    public void createUser_returns_400_when_email_isnt_filled() throws Exception{

        String body = "{ \"pseudo\": \"Tora\", \"email\": null," + 
                        "  \"password\": \"green-mechanic\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType("application/json")
                .content(body))
                    .andExpect(status().is(400));

    }

    @Test
    public void createUser_returns_400_when_password_isnt_filled() throws Exception{

        String body = "{ \"pseudo\": \"Tora\", \"email\": \"tora.lockfire@gmail.com\"," + 
                        "  \"password\": null}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType("application/json")
                .content(body))
                    .andExpect(status().is(400));

    }

    @Test
    public void createUser_returns_400_when_password_is_less_than_6_characters() throws Exception{

        String body = "{ \"pseudo\": \"Tora\", \"email\": \"tora.lockfire@gmail.com\"," + 
                        "  \"password\": \"green\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType("application/json")
                .content(body))
                    .andExpect(status().is(400));

    }





}
