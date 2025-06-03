package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ----- findById -----

    @Test
    void findById_found() {
        Session session = new Session();
        session.setId(1L);
        SessionDto dto = new SessionDto();
        dto.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void findById_notFound() {
        when(sessionService.getById(99L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("99");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void findById_badRequest() {
        ResponseEntity<?> response = sessionController.findById("abc");

        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    // ----- findAll -----

    @Test
    void findAll_shouldReturnList() {
        Session s1 = new Session();
        s1.setId(1L);
        Session s2 = new Session();
        s2.setId(2L);

        List<Session> sessions = Arrays.asList(s1, s2);

        SessionDto dto1 = new SessionDto();
        dto1.setId(1L);
        SessionDto dto2 = new SessionDto();
        dto2.setId(2L);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(Arrays.asList(dto1, dto2));

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        List<?> body = (List<?>) response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
    }

    // ----- create -----

    @Test
    void create_shouldReturnCreatedSession() {
        SessionDto inputDto = new SessionDto();
        inputDto.setName("Session Test");

        Session entity = new Session();
        entity.setName("Session Test");

        Session saved = new Session();
        saved.setId(10L);
        saved.setName("Session Test");

        SessionDto outputDto = new SessionDto();
        outputDto.setId(10L);
        outputDto.setName("Session Test");

        when(sessionMapper.toEntity(inputDto)).thenReturn(entity);
        when(sessionService.create(entity)).thenReturn(saved);
        when(sessionMapper.toDto(saved)).thenReturn(outputDto);

        ResponseEntity<?> response = sessionController.create(inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(outputDto, response.getBody());
    }

    // ----- update -----

    @Test
    void update_shouldReturnUpdatedSession() {
        SessionDto inputDto = new SessionDto();
        inputDto.setName("Updated Name");

        Session entity = new Session();
        entity.setName("Updated Name");

        Session updated = new Session();
        updated.setId(2L);
        updated.setName("Updated Name");

        SessionDto outputDto = new SessionDto();
        outputDto.setId(2L);
        outputDto.setName("Updated Name");

        when(sessionMapper.toEntity(inputDto)).thenReturn(entity);
        when(sessionService.update(2L, entity)).thenReturn(updated);
        when(sessionMapper.toDto(updated)).thenReturn(outputDto);

        ResponseEntity<?> response = sessionController.update("2", inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(outputDto, response.getBody());
    }

    @Test
    void update_badRequest_invalidId() {
        ResponseEntity<?> response = sessionController.update("abc", new SessionDto());

        assertEquals(400, response.getStatusCodeValue());
    }

    // ----- delete -----

    @Test
    void delete_shouldReturnOkIfDeleted() {
        Session session = new Session();
        session.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).delete(1L);
    }

    @Test
    void delete_notFound() {
        when(sessionService.getById(99L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("99");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void delete_badRequest_invalidId() {
        ResponseEntity<?> response = sessionController.save("abc");

        assertEquals(400, response.getStatusCodeValue());
    }

    // ----- participate -----

    @Test
    void participate_shouldReturnOk() {
        doNothing().when(sessionService).participate(1L, 2L);

        ResponseEntity<?> response = sessionController.participate("1", "2");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void participate_badRequest_invalidIds() {
        ResponseEntity<?> response = sessionController.participate("abc", "xyz");

        assertEquals(400, response.getStatusCodeValue());
    }

    // ----- noLongerParticipate -----

    @Test
    void noLongerParticipate_shouldReturnOk() {
        doNothing().when(sessionService).noLongerParticipate(1L, 2L);

        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void noLongerParticipate_badRequest_invalidIds() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("abc", "xyz");

        assertEquals(400, response.getStatusCodeValue());
    }
}
