package com.mantzavelas.tripassistantapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantzavelas.tripassistantapi.TestWebConfiguration;
import com.mantzavelas.tripassistantapi.models.User;
import com.mantzavelas.tripassistantapi.models.UserCredentials;
import com.mantzavelas.tripassistantapi.repositories.UserRepository;
import com.mantzavelas.tripassistantapi.resources.UserResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestWebConfiguration.class
)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        User user = new User();
        user.setFirstName("George");
        user.setLastName("Doe");
        user.setUsername("gdoe");
        user.setPassword(passwordEncoder.encode("george@doe123"));

        userRepository.save(user);
    }

    //
    // SIGNUP TESTS
    @Test
    public void signUpWithExistingUsername() throws Exception {
        UserResource resource = new UserResource();
        resource.setFirstName("George");
        resource.setLastName("Doe");
        resource.setUsername("gdoe");
        resource.setPassword(passwordEncoder.encode("george@doe123"));

        String json = mapper.writeValueAsString(resource);

        mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void successfullSignUp() throws Exception {
        UserResource resource = new UserResource();
        resource.setFirstName("John");
        resource.setLastName("Doe");
        resource.setUsername("johndoe");
        resource.setPassword("john@doe123");


        String json = mapper.writeValueAsString(resource);

        mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    //
    //SIGNIN TESTS
    @Test
    public void unauthorizedResponseForSignInWrongCredentials() throws Exception {
        UserCredentials userCredentials = new UserCredentials("invalid_username", "invalid_password");

        String json = mapper.writeValueAsString(userCredentials);

        mvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void unAuthorizedResponseForEmptyCredentials() throws Exception {
        String json = mapper.writeValueAsString(new UserCredentials());
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void successfullSignIn() throws Exception {
        UserCredentials userCredentials = new UserCredentials("gdoe", "george@doe123");

        String json = mapper.writeValueAsString(userCredentials);
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }
}