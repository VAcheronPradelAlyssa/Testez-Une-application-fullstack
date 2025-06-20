package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("integration@test.com");
        user.setPassword("password");
        user.setFirstName("Alyssa");
        user.setLastName("Test");
        user.setAdmin(false);
        savedUser = userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    // Vérifie qu'on récupère un utilisateur existant (200 attendu)
    @Test
    @WithMockUser(username = "integration@test.com")
    void testFindById_Success() throws Exception {
        User user = userRepository.findByEmail("integration@test.com").orElseThrow();

        mockMvc.perform(get("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("integration@test.com")))
                .andExpect(jsonPath("$.id", is(user.getId().intValue())));
    }

    // Vérifie qu'on obtient 404 si l'utilisateur n'existe pas
    @Test
    @WithMockUser(username = "integration@test.com")
    void testFindById_NotFound() throws Exception {
        mockMvc.perform(get("/api/user/999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Vérifie qu'on obtient 400 si l'id n'est pas valide
    @Test
    @WithMockUser(username = "integration@test.com")
    void testFindById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/user/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Vérifie que la suppression d'un utilisateur fonctionne (200 attendu)
    @Test
    @WithMockUser(username = "integration@test.com")
    void testDelete_Success() throws Exception {
        mockMvc.perform(delete("/api/user/" + savedUser.getId()))
                .andExpect(status().isOk());

        // Vérifier qu'il a bien été supprimé
        mockMvc.perform(get("/api/user/" + savedUser.getId()))
                .andExpect(status().isNotFound());
    }
    
    // Vérifie qu'un utilisateur ne peut pas supprimer un autre utilisateur (401 attendu)
    @Test
    @WithMockUser(username = "other@test.com")
    void testDelete_Unauthorized() throws Exception {
        mockMvc.perform(delete("/api/user/" + savedUser.getId()))
                .andExpect(status().isUnauthorized());
    }

    // Vérifie qu'une suppression sans authentification retourne 401
    @Test
    void testDelete_Unauthenticated() throws Exception {
        mockMvc.perform(delete("/api/user/" + savedUser.getId()))
                .andExpect(status().isUnauthorized());
    }
}