package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

@SpringBootTest
@Transactional // rollback après chaque test
class SessionServiceIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private Session session1;

   @BeforeEach
void setUp() {
    userRepository.deleteAll();
    sessionRepository.deleteAll();

    user1 = new User();
    user1.setEmail("user1@test.com");
    user1.setFirstName("user1");
    user1.setLastName("Test");
    user1.setPassword("pass"); // en vrai, hashé
    user1.setAdmin(false);
    user1 = userRepository.save(user1);

    session1 = new Session();
session1.setName("Session Test");
session1.setDescription("desc");
session1.setDate(java.util.Date.from(java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
session1.setUsers(new ArrayList<>()); 
session1 = sessionRepository.save(session1);
}
   @Test
void createSession_shouldPersistSession() {
    Session newSession = new Session();
    newSession.setName("Nouvelle session");
    newSession.setDescription("desc"); // obligatoire
    newSession.setDate(java.util.Date.from(java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant())); // obligatoire

    Session saved = sessionService.create(newSession);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo("Nouvelle session");
}
    @Test
    void participate_shouldAddUserToSession() {
        sessionService.participate(session1.getId(), user1.getId());

        Session updated = sessionRepository.findById(session1.getId()).orElseThrow();
        assertThat(updated.getUsers()).extracting("id").contains(user1.getId());
    }

    @Test
    void participate_whenUserAlreadyParticipates_shouldThrow() {
        sessionService.participate(session1.getId(), user1.getId());

        assertThatThrownBy(() -> sessionService.participate(session1.getId(), user1.getId()))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    void participate_whenSessionOrUserNotFound_shouldThrow() {
        Long unknownId = 9999L;
        assertThatThrownBy(() -> sessionService.participate(unknownId, user1.getId()))
            .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> sessionService.participate(session1.getId(), unknownId))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void noLongerParticipate_shouldRemoveUserFromSession() {
        sessionService.participate(session1.getId(), user1.getId());
        sessionService.noLongerParticipate(session1.getId(), user1.getId());

        Session updated = sessionRepository.findById(session1.getId()).orElseThrow();
        assertThat(updated.getUsers()).extracting("id").doesNotContain(user1.getId());
    }

    @Test
    void noLongerParticipate_whenUserNotParticipating_shouldThrow() {
        assertThatThrownBy(() -> sessionService.noLongerParticipate(session1.getId(), user1.getId()))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    void delete_shouldRemoveSession() {
        sessionService.delete(session1.getId());

        assertThat(sessionRepository.findById(session1.getId())).isEmpty();
    }
}
