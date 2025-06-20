package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private SessionService service;

    private Session sampleSession;
    private User sampleUser;

    @BeforeEach
    void init() {
        sampleUser = new User();
        sampleUser.setId(42L);

        sampleSession = new Session();
        sampleSession.setId(100L);
        sampleSession.setUsers(new ArrayList<>());
    }

    // --- CREATE ---

    @Test
    // Vérifie que la création d'une session appelle bien le repository et retourne la session
    void create_shouldSaveAndReturn() {
        when(sessionRepo.save(sampleSession)).thenReturn(sampleSession);

        Session result = service.create(sampleSession);

        assertThat(result).isSameAs(sampleSession);
        verify(sessionRepo).save(sampleSession);
    }

    // --- DELETE ---

    @Test
    // Vérifie que la suppression d'une session appelle bien le repository
    void delete_shouldInvokeRepository() {
        service.delete(100L);
        verify(sessionRepo).deleteById(100L);
    }

    // --- FIND ALL ---

    @Test
    // Vérifie que findAll retourne bien la liste des sessions
    void findAll_shouldReturnList() {
        List<Session> all = List.of(sampleSession);
        when(sessionRepo.findAll()).thenReturn(all);

        List<Session> result = service.findAll();

        assertThat(result).isEqualTo(all);
        verify(sessionRepo).findAll();
    }

    // --- GET BY ID ---

    @Test
    // Vérifie que getById retourne la session si elle existe
    void getById_existing_shouldReturnSession() {
        when(sessionRepo.findById(100L)).thenReturn(Optional.of(sampleSession));

        Session result = service.getById(100L);

        assertThat(result).isSameAs(sampleSession);
        verify(sessionRepo).findById(100L);
    }

    @Test
    // Vérifie que getById retourne null si la session n'existe pas
    void getById_missing_shouldReturnNull() {
        when(sessionRepo.findById(100L)).thenReturn(Optional.empty());

        Session result = service.getById(100L);

        assertThat(result).isNull();
        verify(sessionRepo).findById(100L);
    }

    // --- UPDATE ---

    @Test
    // Vérifie que update sauvegarde et retourne la session mise à jour
    void update_shouldSaveAndReturnUpdated() {
        Session toUpdate = new Session();
        toUpdate.setId(100L);
        toUpdate.setDescription("old");
        Session updated = new Session();
        updated.setId(100L);
        updated.setDescription("new");

        when(sessionRepo.save(updated)).thenReturn(updated);

        Session result = service.update(100L, updated);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getDescription()).isEqualTo("new");
        verify(sessionRepo).save(updated);
    }

    // --- PARTICIPATE NEGATIFS ---

    @Test
    // Vérifie qu'une exception est levée si la session n'existe pas lors de la participation
    void participate_missingSession() {
        when(sessionRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.participate(1L, 2L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    // Vérifie qu'une exception est levée si l'utilisateur n'existe pas lors de la participation
    void participate_missingUser() {
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(sampleSession));
        when(userRepo.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.participate(1L, 2L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    // Vérifie qu'une exception est levée si l'utilisateur participe déjà à la session
    void participate_alreadyJoined() {
        sampleSession.getUsers().add(sampleUser);
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(sampleSession));
        when(userRepo.findById(42L)).thenReturn(Optional.of(sampleUser));

        assertThatThrownBy(() -> service.participate(1L, 42L))
            .isInstanceOf(BadRequestException.class);
    }

    // --- PARTICIPATE POSITIF ---

    @Test
    // Vérifie qu'un utilisateur peut participer à une session
    void participate_success() {
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(sampleSession));
        when(userRepo.findById(42L)).thenReturn(Optional.of(sampleUser));
        when(sessionRepo.save(sampleSession)).thenReturn(sampleSession);

        service.participate(1L, 42L);

        assertThat(sampleSession.getUsers()).contains(sampleUser);
        verify(sessionRepo).save(sampleSession);
    }

    // --- NO LONGER PARTICIPATE NEGATIFS ---

    @Test
    // Vérifie qu'une exception est levée si la session n'existe pas lors du retrait
    void leave_missingSession() {
        when(sessionRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.noLongerParticipate(1L, 42L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    // Vérifie qu'une exception est levée si l'utilisateur n'est pas inscrit à la session
    void leave_notJoined() {
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(sampleSession));

        assertThatThrownBy(() -> service.noLongerParticipate(1L, 42L))
            .isInstanceOf(BadRequestException.class);
    }

    // --- NO LONGER PARTICIPATE POSITIF ---

    @Test
    // Vérifie qu'un utilisateur peut se retirer d'une session
    void leave_success() {
        sampleSession.getUsers().add(sampleUser);
        when(sessionRepo.findById(1L)).thenReturn(Optional.of(sampleSession));
        when(sessionRepo.save(sampleSession)).thenReturn(sampleSession);

        service.noLongerParticipate(1L, 42L);

        assertThat(sampleSession.getUsers()).doesNotContain(sampleUser);
        verify(sessionRepo).save(sampleSession);
    }
}
