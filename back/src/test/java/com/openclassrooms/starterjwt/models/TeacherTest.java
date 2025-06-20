package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    @Test
    // Vérifie les getters et setters de la classe Teacher
    void testSettersAndGetters() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Dupont");
        teacher.setFirstName("Alice");
        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertEquals(1L, teacher.getId());
        assertEquals("Dupont", teacher.getLastName());
        assertEquals("Alice", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    // Vérifie les branches particulières de equals et hashCode (id null, id égal, types différents)
    void testEqualsAndHashCodeBranches() {
        LocalDateTime now = LocalDateTime.now();

        // id null dans les deux objets
        Teacher tNull1 = new Teacher(null, "Dupont", "Alice", now, now);
        Teacher tNull2 = new Teacher(null, "Dupont", "Alice", now, now);
        assertEquals(tNull1, tNull2);
        assertEquals(tNull1.hashCode(), tNull2.hashCode());

        // id null vs id non null
        Teacher tWithId = new Teacher(2L, "Dupont", "Alice", now, now);
        assertNotEquals(tNull1, tWithId);
        assertNotEquals(tWithId, tNull1);

        // id égal, autres champs différents
        Teacher tDiff = new Teacher(2L, "Martin", "Bob", now, now);
        assertEquals(tWithId, tDiff); // equals ne regarde que l'id
        assertEquals(tWithId.hashCode(), tDiff.hashCode());

        // Comparaison avec un autre type
        assertNotEquals(tWithId, "string");
        // Comparaison avec null
        assertNotEquals(tWithId, null);
    }

    @Test
    // Vérifie la méthode toString et le builder de Teacher
    void testToStringAndBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(3L)
                .lastName("Martin")
                .firstName("Bob")
                .createdAt(now)
                .updatedAt(now)
                .build();

        String str = teacher.toString();
        assertNotNull(str);
        assertTrue(str.contains("Martin"));
        assertTrue(str.contains("Bob"));
    }
}