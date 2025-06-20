package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.ExpiredJwtException;


import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Injection manuelle des propriétés
        jwtUtils.jwtSecret = "testSecretKey1234567890";
        jwtUtils.jwtExpirationMs = 1000 * 60 * 60; // 1h
    }
    @Test
    // Vérifie qu'un token JWT généré est valide et que l'email est bien extrait
    void generateAndValidateJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L, "test@user.com", "First", "Last", false, "password"
        );
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("test@user.com", jwtUtils.getUserNameFromJwtToken(token));
    }
    @Test
    // Vérifie que la validation échoue si la signature du token est invalide
    void validateJwtToken_invalidSignature() {
        String fakeToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("test@user.com")
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(fakeToken));
    }

    @Test
    // Vérifie que la validation échoue avec un token mal formé
    void validateJwtToken_malformedToken() {
        String malformedToken = "not.a.jwt.token";
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }
    @Test
    // Vérifie que la validation échoue avec un token expiré
    void validateJwtToken_expiredToken() {
        // Génère un token déjà expiré
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("test@user.com")
                .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000)) // déjà expiré
                .signWith(SignatureAlgorithm.HS512, jwtUtils.jwtSecret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    // Vérifie que la validation échoue avec un token non supporté
    void validateJwtToken_unsupportedToken() {
        // Simule un token non supporté (par exemple, un JWT signé avec un algo non supporté)
        String unsupportedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHVzZXIuY29tIn0.signature";
        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }

    @Test
    // Vérifie que la validation échoue avec un token vide ou null
    void validateJwtToken_emptyToken() {
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken(null));
    }
}