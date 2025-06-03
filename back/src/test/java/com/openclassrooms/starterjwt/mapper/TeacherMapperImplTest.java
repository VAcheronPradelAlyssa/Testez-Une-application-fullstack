package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperImplTest {

    private TeacherMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new TeacherMapperImpl();
    }

    @Test
    void testToEntity() {
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        Teacher entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void testToDto() {
        Teacher entity = Teacher.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TeacherDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setFirstName("John");
        dto1.setLastName("Doe");

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");

        List<Teacher> entities = mapper.toEntity(Arrays.asList(dto1, dto2));
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals(dto1.getId(), entities.get(0).getId());
        assertEquals(dto2.getId(), entities.get(1).getId());
    }

    @Test
    void testToDtoList() {
        Teacher entity1 = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();
        Teacher entity2 = Teacher.builder().id(2L).firstName("Jane").lastName("Smith").build();

        List<TeacherDto> dtos = mapper.toDto(Arrays.asList(entity1, entity2));
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(entity1.getId(), dtos.get(0).getId());
        assertEquals(entity2.getId(), dtos.get(1).getId());
    }

    @Test
    void testNullCases() {
        assertNull(mapper.toEntity((TeacherDto) null));
        assertNull(mapper.toDto((Teacher) null));
        assertNull(mapper.toEntity((List<TeacherDto>) null));
        assertNull(mapper.toDto((List<Teacher>) null));
    }
}