package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private UserRepository userRepository;
    private AuthController controller;
    private ObjectMapper objectMapper; // Optionnel, si besoin

    @BeforeEach
    void setup() {
        authenticationManager = mock(AuthenticationManager.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtils = mock(JwtUtils.class);
        userRepository = mock(UserRepository.class);
        controller = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void cleanup() {
        // Nettoyage si besoin (ex: reset mocks, données temporaires, etc.)
        // Ici on peut faire reset des mocks si on veut (optionnel)
        reset(authenticationManager, passwordEncoder, jwtUtils, userRepository);
    }

    @Test
    void authenticateUser_success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getFirstName()).thenReturn("John");
        when(userDetails.getLastName()).thenReturn("Doe");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(new User("user@example.com", "Doe", "John", "encoded", false)));

        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        JwtResponse jwt = (JwtResponse) response.getBody();
        assertNotNull(jwt);
        assertEquals("user@example.com", jwt.getUsername());
        assertFalse(jwt.getAdmin());
    }

    @Test
    void authenticateUser_userNotFound_setsIsAdminFalse() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("ghost@example.com");
        loginRequest.setPassword("badpass");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("ghost@example.com");
        when(userDetails.getId()).thenReturn(42L);
        when(userDetails.getFirstName()).thenReturn("Ghost");
        when(userDetails.getLastName()).thenReturn("User");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        JwtResponse jwt = (JwtResponse) response.getBody();
        assertNotNull(jwt);
        assertEquals("ghost@example.com", jwt.getUsername());
        assertFalse(jwt.getAdmin());
    }

    @Test
    void authenticateUser_invalidPassword_throwsException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("wrong");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> controller.authenticateUser(loginRequest));
    }

    @Test
    void registerUser_success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("new@user.com");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("new@user.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        ResponseEntity<?> response = controller.registerUser(signupRequest);
        MessageResponse msg = (MessageResponse) response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", msg.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_emailAlreadyExists_shouldReturnBadRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("existing@user.com");

        when(userRepository.existsByEmail("existing@user.com")).thenReturn(true);

        ResponseEntity<?> response = controller.registerUser(signupRequest);

        assertEquals(400, response.getStatusCodeValue());
        MessageResponse msg = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already taken!", msg.getMessage());
        verify(userRepository, never()).save(any());
    }
}
