package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
  @Autowired
private JdbcTemplate jdbcTemplate;

@BeforeEach
void setup() {
    jdbcTemplate.execute("DELETE FROM PARTICIPATE");
    userRepository.deleteAll();
}


    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("DELETE FROM PARTICIPATE");
        userRepository.deleteAll();
    }

    // Vérifie que l'inscription d'un nouvel utilisateur fonctionne et retourne un statut 200.
    @Test
    void registerUser_shouldReturn200() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("integration@user.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Integration");
        signupRequest.setPassword("securepass");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());
    }

    // Vérifie que la connexion d'un utilisateur existant retourne bien un token JWT.
    @Test
    void login_shouldReturnToken() throws Exception {
        // Prépare un utilisateur en base pour tester la connexion
        User user = new User();
        user.setEmail("integration@user.com");
        user.setFirstName("Test");
        user.setLastName("Integration");
        user.setPassword(passwordEncoder.encode("securepass")); // Encode le mot de passe
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("integration@user.com");
        loginRequest.setPassword("securepass");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JwtResponse jwt = objectMapper.readValue(responseBody, JwtResponse.class);

        assertNotNull(jwt.getToken());
        assertEquals("integration@user.com", jwt.getUsername());
    }
}
