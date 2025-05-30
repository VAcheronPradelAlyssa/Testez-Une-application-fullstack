package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void delete_shouldCallRepository() {
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void findById_shouldReturnUser_whenExists() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.findById(1L);
        assertEquals(user, result);
    }

    @Test
    void findById_shouldReturnNull_whenNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        User result = userService.findById(1L);
        assertNull(result);
    }
}