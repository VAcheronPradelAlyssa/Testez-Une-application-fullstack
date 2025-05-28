package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:script.sql")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void authenticateUserTest() throws Exception {

        String email = "yoga@studio.com";

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail(email);
        loginRequest.setPassword("Mypassword8$");

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andDo(r -> {
                String result = r.getResponse().getContentAsString();
                JwtResponse user = objectMapper.readValue(result, JwtResponse.class);
                assertEquals(email, user.getUsername());
            });

    }

   @Test
public void authenticateUser_UserNotFound() throws Exception {
    String email = "notfound@studio.com";

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword("Mypassword8$");

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
}

    @Nested
    public class registerUserTest{
        @Test
        void shouldRegister() throws Exception {

            String newEmail = "yoga2@studio.com";

            // GIVEN
            SignupRequest signUpRequest = new SignupRequest();
            signUpRequest.setEmail(newEmail);
            signUpRequest.setFirstName("new");
            signUpRequest.setLastName("one");
            signUpRequest.setPassword("password");

            // WHEN & THEN
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpRequest)))
                    .andExpect(status().isOk());
        }
        @Test
        void shouldBadRequest() throws Exception {

            // GIVEN
            SignupRequest signUpRequest = new SignupRequest();
            signUpRequest.setEmail(null);
            signUpRequest.setFirstName("Test");
            signUpRequest.setLastName("User");
            signUpRequest.setPassword("password");

            // WHEN & THEN
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpRequest)))
                    .andExpect(status().isBadRequest());
        }
        
        @Test
        void shouldBadRequestEmailAlreadyExists() throws Exception {

            // GIVEN
            SignupRequest signUpRequest = new SignupRequest();
            signUpRequest.setEmail("yoga@studio.com");
            signUpRequest.setFirstName("Test");
            signUpRequest.setLastName("User");
            signUpRequest.setPassword("password");

            // WHEN & THEN
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpRequest)))
                    .andExpect(status().isBadRequest());
        }
    }
@Test
void authenticateUser_userNotFound_setsIsAdminFalse() {
    // Arrange
    AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    JwtUtils jwtUtils = mock(JwtUtils.class);
    UserRepository userRepository = mock(UserRepository.class);

    AuthController controller = new AuthController(authenticationManager, null, jwtUtils, userRepository);

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("notfound@studio.com");
    loginRequest.setPassword("Mypassword8$");

    Authentication authentication = mock(Authentication.class);
    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("notfound@studio.com");
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");
    when(userRepository.findByEmail("notfound@studio.com")).thenReturn(Optional.empty());

    // Act
    ResponseEntity<?> response = controller.authenticateUser(loginRequest);

    // Assert
    JwtResponse jwtResponse = (JwtResponse) response.getBody();
    assertNotNull(jwtResponse);
    assertFalse(jwtResponse.getAdmin());
}
}