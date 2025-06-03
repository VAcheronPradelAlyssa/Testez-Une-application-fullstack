package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Helpers pour créer entités valides communes aux tests
    private Teacher createAndSaveTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        return teacherRepository.save(teacher);
    }

    private User createAndSaveUser(String email, String firstName, String lastName) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword("pass");
        return userRepository.save(user);
    }

    @BeforeEach
    void setup() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void cleanup() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    // ------------------------
    // Tests positifs
    // ------------------------

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void createAndGetSession_shouldReturnCreatedSession() throws Exception {
        Teacher teacher = createAndSaveTeacher();
        User user1 = createAndSaveUser("user1@test.com", "User", "One");
        User user2 = createAndSaveUser("user2@test.com", "User", "Two");

        SessionDto dto = new SessionDto();
        dto.setName("Session Test");
        dto.setDate(new Date());
        dto.setTeacher_id(teacher.getId());
        dto.setDescription("Description valide pour la session.");
        dto.setUsers(List.of(user1.getId(), user2.getId()));

        String json = objectMapper.writeValueAsString(dto);

        // Création session
        MvcResult resultPost = mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Récupération session créée
        String responseJson = resultPost.getResponse().getContentAsString();
        SessionDto createdSession = objectMapper.readValue(responseJson, SessionDto.class);
        Long id = createdSession.getId();

        mockMvc.perform(get("/api/session/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Session Test"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getAllSessions_shouldReturnListContainingSessions() throws Exception {
        Teacher teacher = createAndSaveTeacher();

        SessionDto dto = new SessionDto();
        dto.setName("Session 1");
        dto.setDate(new Date());
        dto.setTeacher_id(teacher.getId());
        dto.setDescription("Desc");

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Session 1"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void deleteSession_shouldRemoveSessionSuccessfully() throws Exception {
        Teacher teacher = createAndSaveTeacher();

        SessionDto dto = new SessionDto();
        dto.setName("Session à supprimer");
        dto.setDate(new Date());
        dto.setTeacher_id(teacher.getId());
        dto.setDescription("Desc");

        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        SessionDto created = objectMapper.readValue(result.getResponse().getContentAsString(), SessionDto.class);

        mockMvc.perform(delete("/api/session/{id}", created.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/{id}", created.getId()))
                .andExpect(status().isNotFound());
    }

    // ------------------------
    // Tests négatifs (cas d'erreurs)
    // ------------------------

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/session/9999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_badRequest_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/session/abc"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void createSession_withMissingFields_shouldReturnBadRequest() throws Exception {
        SessionDto dto = new SessionDto(); // champs vides
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void createSession_withInvalidTeacher_shouldReturnOk() throws Exception {
        // On suppose que le contrôleur accepte un teacher_id inexistant sans erreur
        SessionDto dto = new SessionDto();
        dto.setName("Session sans teacher");
        dto.setDate(new Date());
        dto.setTeacher_id(9999L); // id inexistant
        dto.setDescription("Desc");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void createSession_withInvalidUser_shouldReturnServerErrorOrThrow() throws Exception {
        Teacher teacher = createAndSaveTeacher();

        SessionDto dto = new SessionDto();
        dto.setName("Session avec mauvais user");
        dto.setDate(new Date());
        dto.setTeacher_id(teacher.getId());
        dto.setDescription("Desc");
        dto.setUsers(List.of(9999L)); // id user inexistant
        String json = objectMapper.writeValueAsString(dto);

        try {
            mockMvc.perform(post("/api/session")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andDo(print())
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // OK si exception technique levée côté serveur (ex : NestedServletException)
            assertTrue(e.getCause() instanceof org.springframework.web.util.NestedServletException
                || e instanceof org.springframework.web.util.NestedServletException);
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void deleteSession_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/session/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void unauthorizedUser_cannotCreateSession_shouldReturn401() throws Exception {
        SessionDto dto = new SessionDto();
        dto.setName("Session Test");
        dto.setDate(new Date());
        dto.setDescription("Desc");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }
}
