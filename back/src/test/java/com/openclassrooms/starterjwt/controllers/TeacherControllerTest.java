package com.openclassrooms.starterjwt.controllers;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
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

    // ----------- findById tests ------------

    // Vérifie qu'on récupère un enseignant existant (200 attendu)
    @Test
    void findById_shouldReturnTeacher_whenFound() throws Exception {
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

        verify(teacherService, times(1)).findById(1L);
        verify(teacherMapper, times(1)).toDto(teacher);
    }

     // Vérifie qu'on obtient 404 si l'enseignant n'existe pas
    @Test
    void findById_shouldReturn404_whenNotFound() throws Exception {
        when(teacherService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(teacherService, times(1)).findById(99L);
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    // Vérifie qu'on obtient 400 si l'id n'est pas valide
    @Test
    void findById_shouldReturn400_whenInvalidId() throws Exception {
        mockMvc.perform(get("/api/teacher/invalidId")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(teacherService, never()).findById(anyLong());
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    // ----------- findAll tests ------------

    // Vérifie que la liste des enseignants est bien retournée
    @Test
    void findAll_shouldReturnListOfTeachers_whenNotEmpty() throws Exception {
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
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Margot"));

        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(anyList());
    }
    
    // Vérifie qu'on obtient une liste vide si aucun enseignant n'existe
    @Test
    void findAll_shouldReturnEmptyList_whenNoTeachers() throws Exception {
        when(teacherService.findAll()).thenReturn(Collections.emptyList());
        when(teacherMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teacher")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(Collections.emptyList());
    }
}
