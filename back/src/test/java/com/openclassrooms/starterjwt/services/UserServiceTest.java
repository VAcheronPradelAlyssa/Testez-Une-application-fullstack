package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        Long userId = 1L;

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findById_shouldReturnNull_whenUserDoesNotExist() {
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = userService.findById(userId);

        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }
}
