package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        // Nettoyage du contexte Security avant chaque test
        SecurityContextHolder.clearContext();
    }

    // Vérifie qu'on récupère un utilisateur existant (200 attendu)
    @Test
    void testFindById_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
        verify(userService).findById(1L);
    }

    // Vérifie qu'on obtient 404 si l'utilisateur n'existe pas
    @Test
    void testFindById_UserNotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    // Vérifie qu'on obtient 400 si l'id n'est pas valide
    @Test
    void testFindById_InvalidId() {
        ResponseEntity<?> response = userController.findById("abc");

        assertEquals(400, response.getStatusCodeValue());
    }

     // Vérifie que la suppression d'un utilisateur fonctionne (200 attendu)
    @Test
    void testDelete_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "password", Collections.emptyList());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(userService).delete(1L);
    }

    // Vérifie qu'on obtient 404 si l'utilisateur à supprimer n'existe pas
    @Test
    void testDelete_UserNotFound() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "password", Collections.emptyList());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    // Vérifie qu'un utilisateur ne peut pas supprimer un autre utilisateur (401 attendu)
    @Test
    void testDelete_Unauthorized() {
        User user = new User();
        user.setId(1L);
        user.setEmail("other@example.com");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "password", Collections.emptyList());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(401, response.getStatusCodeValue());
    }

    // Vérifie qu'on obtient 400 si l'id pour la suppression n'est pas valide
    @Test
    void testDelete_InvalidId() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "password", Collections.emptyList());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        ResponseEntity<?> response = userController.save("abc");

        assertEquals(400, response.getStatusCodeValue());
    }
}
