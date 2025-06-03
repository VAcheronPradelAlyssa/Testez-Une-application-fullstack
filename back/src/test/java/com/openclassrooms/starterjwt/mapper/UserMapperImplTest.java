package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperImplTest {

    private final UserMapperImpl mapper = new UserMapperImpl();

    @Test
    void testToEntity() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@test.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPassword("password");
        dto.setAdmin(true);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        User entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getPassword(), entity.getPassword());
        assertEquals(dto.isAdmin(), entity.isAdmin());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void testToDto() {
        User entity = User.builder()
                .id(2L)
                .email("jane@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("secret")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getPassword(), dto.getPassword());
        assertEquals(entity.isAdmin(), dto.isAdmin());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        UserDto dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setEmail("user1@test.com");
        dto1.setFirstName("User");
        dto1.setLastName("One");
        dto1.setPassword("pass1");
        dto1.setAdmin(false);
        dto1.setCreatedAt(LocalDateTime.now());
        dto1.setUpdatedAt(LocalDateTime.now());

        UserDto dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setEmail("user2@test.com");
        dto2.setFirstName("User");
        dto2.setLastName("Two");
        dto2.setPassword("pass2");
        dto2.setAdmin(false);
        dto2.setCreatedAt(LocalDateTime.now());
        dto2.setUpdatedAt(LocalDateTime.now());

        List<UserDto> dtoList = Arrays.asList(dto1, dto2);
        List<User> entityList = mapper.toEntity(dtoList);

        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertEquals(dto1.getId(), entityList.get(0).getId());
        assertEquals(dto2.getId(), entityList.get(1).getId());
    }

    @Test
    void testToDtoList() {
        User user1 = User.builder()
                .id(1L)
                .email("user1@test.com")
                .firstName("User")
                .lastName("One")
                .password("pass1")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@test.com")
                .firstName("User")
                .lastName("Two")
                .password("pass2")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<User> entityList = Arrays.asList(user1, user2);
        List<UserDto> dtoList = mapper.toDto(entityList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(user1.getId(), dtoList.get(0).getId());
        assertEquals(user2.getId(), dtoList.get(1).getId());
    }

    @Test
    void testNullCases() {
        assertNull(mapper.toEntity((UserDto) null));
        assertNull(mapper.toDto((User) null));
        assertNull(mapper.toEntity((List<UserDto>) null));
        assertNull(mapper.toDto((List<User>) null));
    }
}