package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    @Test
    void testSettersAndGetters() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga");
        Date date = new Date();
        session.setDate(date);
        session.setDescription("Cours de yoga");
        Teacher teacher = new Teacher();
        session.setTeacher(teacher);
        List<User> users = new ArrayList<>();
        session.setUsers(users);
        LocalDateTime now = LocalDateTime.now();
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        assertEquals(1L, session.getId());
        assertEquals("Yoga", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Cours de yoga", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();

        Session s1 = new Session(1L, "Yoga", date, "desc", teacher, users, now, now);
        Session s2 = new Session(1L, "Yoga", date, "desc", teacher, users, now, now);
        Session s3 = new Session(2L, "Pilates", date, "desc2", null, null, now, now);

        // equals
        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertNotEquals(s1, null);
        assertNotEquals(s1, "string");
        assertEquals(s1, s1);

        // hashCode
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1.hashCode(), s3.hashCode());
    }

    @Test
    void testToStringAndBuilder() {
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();

        Session session = Session.builder()
                .id(10L)
                .name("Méditation")
                .date(date)
                .description("Relaxation")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        String str = session.toString();
        assertNotNull(str);
        assertTrue(str.contains("Méditation"));
        assertTrue(str.contains("Relaxation"));
    }
    @Test
void testEqualsAndHashCodeBranches() {
    Date date = new Date();
    LocalDateTime now = LocalDateTime.now();

    // id null dans les deux objets
    Session sNull1 = new Session(null, "A", date, "desc", null, null, now, now);
    Session sNull2 = new Session(null, "A", date, "desc", null, null, now, now);
    assertEquals(sNull1, sNull2);
    assertEquals(sNull1.hashCode(), sNull2.hashCode());

    // id null vs id non null
    Session sWithId = new Session(5L, "A", date, "desc", null, null, now, now);
    assertNotEquals(sNull1, sWithId);
    assertNotEquals(sWithId, sNull1);

    // id égal, autres champs différents
    Session sDiff = new Session(5L, "B", new Date(date.getTime() + 1000), "autre", null, null, now, now);
    assertEquals(sWithId, sDiff); // equals ne regarde que l'id
    assertEquals(sWithId.hashCode(), sDiff.hashCode());

    // Comparaison avec un autre type
    assertNotEquals(sWithId, "string");
    // Comparaison avec null
    assertNotEquals(sWithId, null);
}
}