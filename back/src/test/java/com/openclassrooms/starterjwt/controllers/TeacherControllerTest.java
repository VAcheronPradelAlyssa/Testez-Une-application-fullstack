package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TeacherControllerTest {

    private MockMvc mockMvc;
    private TeacherService teacherService;
    private TeacherMapper teacherMapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        teacherService = Mockito.mock(TeacherService.class);
        teacherMapper = Mockito.mock(TeacherMapper.class);
        objectMapper = new ObjectMapper();
        TeacherController controller = new TeacherController(teacherService, teacherMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findById_shouldReturnTeacher() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Margot");

        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setFirstName("Margot");

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(dto);

        mockMvc.perform(get("/api/teacher/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Margot"));
    }

    @Test
    void findById_shouldReturn404WhenNotFound() throws Exception {
        when(teacherService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/99")
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
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Margot");

        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setFirstName("Margot");

        when(teacherService.findAll()).thenReturn(List.of(teacher));
        when(teacherMapper.toDto(List.of(teacher))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/teacher")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Margot"));
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoneExists() throws Exception {
        when(teacherService.findAll()).thenReturn(Collections.emptyList());
        when(teacherMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teacher")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
