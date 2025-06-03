package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser 
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher savedTeacher;

    @BeforeEach
    void setup() {
        teacherRepository.deleteAll(); // Nettoie la base avant chaque test

        Teacher teacher = new Teacher();
        teacher.setFirstName("Margot");
        teacher.setLastName("Dupont");

        savedTeacher = teacherRepository.save(teacher);
    }

    @AfterEach
    void cleanup() {
        teacherRepository.deleteAll(); // Nettoie la base après chaque test
    }

    @Test
    void findById_shouldReturnTeacher() throws Exception {
        mockMvc.perform(get("/api/teacher/" + savedTeacher.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Margot"))
                .andExpect(jsonPath("$.lastName").value("Dupont"));
    }

    @Test
    void findById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/teacher/999999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequestWhenInvalidId() throws Exception {
        mockMvc.perform(get("/api/teacher/notANumber")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldReturnListOfTeachers() throws Exception {
        mockMvc.perform(get("/api/teacher")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Margot"));
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoneExists() throws Exception {
        teacherRepository.deleteAll();

        mockMvc.perform(get("/api/teacher")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}