package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructorsAndSetters() {
        // Test constructeur complet
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "a@a.com", "Doe", "John", "pass", true, now, now);
        assertEquals(1L, user.getId());
        assertEquals("a@a.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("pass", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());

        // Test setters
        user.setLastName("Smith");
        user.setFirstName("Jane");
        user.setPassword("newpass");
        user.setEmail("b@b.com");
        user.setAdmin(false);
        LocalDateTime later = now.plusDays(1);
        user.setCreatedAt(later);
        user.setUpdatedAt(later);

        assertEquals("Smith", user.getLastName());
        assertEquals("Jane", user.getFirstName());
        assertEquals("newpass", user.getPassword());
        assertEquals("b@b.com", user.getEmail());
        assertFalse(user.isAdmin());
        assertEquals(later, user.getCreatedAt());
        assertEquals(later, user.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCodeAndToString() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "a@a.com", "Doe", "John", "pass", true, now, now);
        User user2 = new User(1L, "a@a.com", "Doe", "John", "pass", true, now, now);
        User user3 = new User(2L, "b@b.com", "Smith", "Jane", "pass2", false, now, now);

        // equals
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, "string");
        assertEquals(user1, user1);

        // hashCode
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());

        // toString
        String str = user1.toString();
        assertNotNull(str);
        assertTrue(str.contains("a@a.com"));
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("John"));
    }

    @Test
    void testEqualsWithNullId() {
        User user1 = new User(null, "a@a.com", "Doe", "John", "pass", true, null, null);
        User user2 = new User(null, "a@a.com", "Doe", "John", "pass", true, null, null);
        assertEquals(user1, user2);

        User user3 = new User(3L, "a@a.com", "Doe", "John", "pass", true, null, null);
        assertNotEquals(user1, user3);
    }

    @Test
    void testBuilderAndAccessors() {
        User user = User.builder()
                .id(5L)
                .email("c@c.com")
                .lastName("Last")
                .firstName("First")
                .password("pwd")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertEquals(5L, user.getId());
        assertEquals("c@c.com", user.getEmail());
        assertEquals("Last", user.getLastName());
        assertEquals("First", user.getFirstName());
        assertEquals("pwd", user.getPassword());
        assertTrue(user.isAdmin());
    }
   @Test
void testConstructorSansIdEtDatesEtSettersNulls() {
    // Constructeur sans id ni dates
    User user = new User("mail@mail.com", "Doe", "John", "pwd", true);
    assertEquals("mail@mail.com", user.getEmail());
    assertEquals("Doe", user.getLastName());
    assertEquals("John", user.getFirstName());
    assertEquals("pwd", user.getPassword());
    assertTrue(user.isAdmin());

    // Ne PAS tester les setters avec null car @NonNull lève une exception
    // On peut tester que l'exception est bien levée si tu veux :
    assertThrows(NullPointerException.class, () -> user.setEmail(null));
    assertThrows(NullPointerException.class, () -> user.setLastName(null));
    assertThrows(NullPointerException.class, () -> user.setFirstName(null));
    assertThrows(NullPointerException.class, () -> user.setPassword(null));
}

@Test
void testSettersWithVariousValues() {
    User user = new User();
    user.setEmail("test@test.com");
    user.setLastName("Last");
    user.setFirstName("First");
    user.setPassword("pass");
    user.setAdmin(true);

    assertEquals("test@test.com", user.getEmail());
    assertEquals("Last", user.getLastName());
    assertEquals("First", user.getFirstName());
    assertEquals("pass", user.getPassword());
    assertTrue(user.isAdmin());
}
@Test
void testConstructorStringVariants() {
    // Tous les paramètres valides
    User user = new User("mail@mail.com", "Doe", "John", "pwd", true);
    assertEquals("mail@mail.com", user.getEmail());
    assertEquals("Doe", user.getLastName());
    assertEquals("John", user.getFirstName());
    assertEquals("pwd", user.getPassword());
    assertTrue(user.isAdmin());

    // Chaque paramètre @NonNull à null doit lever une exception
    assertThrows(NullPointerException.class, () -> new User(null, "Doe", "John", "pwd", true));
    assertThrows(NullPointerException.class, () -> new User("mail@mail.com", null, "John", "pwd", true));
    assertThrows(NullPointerException.class, () -> new User("mail@mail.com", "Doe", null, "pwd", true));
    assertThrows(NullPointerException.class, () -> new User("mail@mail.com", "Doe", "John", null, true));
}
}