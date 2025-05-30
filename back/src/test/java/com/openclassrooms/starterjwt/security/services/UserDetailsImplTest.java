package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    @Test
    void equals_shouldReturnTrue_whenSameId() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1", "First", "Last", false, "password");
        UserDetailsImpl user2 = new UserDetailsImpl(1L, "user2", "Other", "Other", true, "otherpassword");
        assertEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentId() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1", "First", "Last", false, "password");
        UserDetailsImpl user2 = new UserDetailsImpl(2L, "user2", "Other", "Other", true, "otherpassword");
        assertNotEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalse_whenOtherIsNull() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1", "First", "Last", false, "password");
        assertNotEquals(user1, null);
    }

    @Test
    void equals_shouldReturnTrue_whenSameObject() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1", "First", "Last", false, "password");
        assertEquals(user1, user1);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentClass() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1", "First", "Last", false, "password");
        Object other = new Object();
        assertNotEquals(user1, other);
    }
}