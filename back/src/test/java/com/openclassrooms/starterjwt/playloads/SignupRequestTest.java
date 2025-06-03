package com.openclassrooms.starterjwt.playloads;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validSignupRequest_shouldHaveNoViolations() {
        SignupRequest req = new SignupRequest();
        req.setEmail("test@email.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

   @Test
void blankFields_shouldHaveViolations() {
    SignupRequest req = new SignupRequest();
    req.setEmail("");
    req.setFirstName("");
    req.setLastName("");
    req.setPassword("");

    Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);

    // On vérifie qu'il y a au moins une violation pour chaque champ
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
}

    @Test
    void invalidEmail_shouldHaveViolation() {
        SignupRequest req = new SignupRequest();
        req.setEmail("not-an-email");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shortPassword_shouldHaveViolation() {
        SignupRequest req = new SignupRequest();
        req.setEmail("test@email.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPassword("123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
 @Test
void testEqualsBranchesAndNulls() {
    SignupRequest base = new SignupRequest();
    base.setEmail("a@a.com");
    base.setFirstName("Alice");
    base.setLastName("Dupont");
    base.setPassword("password123");

    // Réflexivité
    assertEquals(base, base);

    // Comparaison avec null
    assertNotEquals(base, null);

    // Comparaison avec un autre type
    assertNotEquals(base, "string");

    // Tous les champs identiques
    SignupRequest same = new SignupRequest();
    same.setEmail("a@a.com");
    same.setFirstName("Alice");
    same.setLastName("Dupont");
    same.setPassword("password123");
    assertEquals(base, same);

    // Un champ différent à chaque fois
    SignupRequest diffEmail = new SignupRequest();
    diffEmail.setEmail("b@b.com");
    diffEmail.setFirstName("Alice");
    diffEmail.setLastName("Dupont");
    diffEmail.setPassword("password123");
    assertNotEquals(base, diffEmail);

    SignupRequest diffFirstName = new SignupRequest();
    diffFirstName.setEmail("a@a.com");
    diffFirstName.setFirstName("Bob");
    diffFirstName.setLastName("Dupont");
    diffFirstName.setPassword("password123");
    assertNotEquals(base, diffFirstName);

    SignupRequest diffLastName = new SignupRequest();
    diffLastName.setEmail("a@a.com");
    diffLastName.setFirstName("Alice");
    diffLastName.setLastName("Martin");
    diffLastName.setPassword("password123");
    assertNotEquals(base, diffLastName);

    SignupRequest diffPassword = new SignupRequest();
    diffPassword.setEmail("a@a.com");
    diffPassword.setFirstName("Alice");
    diffPassword.setLastName("Dupont");
    diffPassword.setPassword("otherpass");
    assertNotEquals(base, diffPassword);

    // Champs à null dans l'un ou l'autre
    SignupRequest nullFields1 = new SignupRequest();
    nullFields1.setEmail(null);
    nullFields1.setFirstName("Alice");
    nullFields1.setLastName("Dupont");
    nullFields1.setPassword("password123");
    assertNotEquals(base, nullFields1);

    SignupRequest nullFields2 = new SignupRequest();
    nullFields2.setEmail("a@a.com");
    nullFields2.setFirstName(null);
    nullFields2.setLastName("Dupont");
    nullFields2.setPassword("password123");
    assertNotEquals(base, nullFields2);

    SignupRequest nullFields3 = new SignupRequest();
    nullFields3.setEmail("a@a.com");
    nullFields3.setFirstName("Alice");
    nullFields3.setLastName(null);
    nullFields3.setPassword("password123");
    assertNotEquals(base, nullFields3);

    SignupRequest nullFields4 = new SignupRequest();
    nullFields4.setEmail("a@a.com");
    nullFields4.setFirstName("Alice");
    nullFields4.setLastName("Dupont");
    nullFields4.setPassword(null);
    assertNotEquals(base, nullFields4);

    // Tous les champs à null dans les deux objets
    SignupRequest allNull1 = new SignupRequest();
    SignupRequest allNull2 = new SignupRequest();
    assertEquals(allNull1, allNull2);

    // Un objet avec tous les champs à null, l'autre non
    assertNotEquals(base, allNull1);
    assertNotEquals(allNull1, base);
}
@Test
void testHashCodeBranches() {
    // Tous les champs non nuls
    SignupRequest full = new SignupRequest();
    full.setEmail("a@a.com");
    full.setFirstName("Alice");
    full.setLastName("Dupont");
    full.setPassword("password123");
    int hashFull = full.hashCode();

    // Tous les champs à null
    SignupRequest allNull = new SignupRequest();
    int hashAllNull = allNull.hashCode();

    // Un champ à null à chaque fois
    SignupRequest nullEmail = new SignupRequest();
    nullEmail.setEmail(null);
    nullEmail.setFirstName("Alice");
    nullEmail.setLastName("Dupont");
    nullEmail.setPassword("password123");
    int hashNullEmail = nullEmail.hashCode();

    SignupRequest nullFirstName = new SignupRequest();
    nullFirstName.setEmail("a@a.com");
    nullFirstName.setFirstName(null);
    nullFirstName.setLastName("Dupont");
    nullFirstName.setPassword("password123");
    int hashNullFirstName = nullFirstName.hashCode();

    SignupRequest nullLastName = new SignupRequest();
    nullLastName.setEmail("a@a.com");
    nullLastName.setFirstName("Alice");
    nullLastName.setLastName(null);
    nullLastName.setPassword("password123");
    int hashNullLastName = nullLastName.hashCode();

    SignupRequest nullPassword = new SignupRequest();
    nullPassword.setEmail("a@a.com");
    nullPassword.setFirstName("Alice");
    nullPassword.setLastName("Dupont");
    nullPassword.setPassword(null);
    int hashNullPassword = nullPassword.hashCode();

    // Les hashCodes doivent être calculés sans lever d'exception
    assertNotEquals(hashFull, hashAllNull); // Probablement différents
    assertNotEquals(hashFull, hashNullEmail);
    assertNotEquals(hashFull, hashNullFirstName);
    assertNotEquals(hashFull, hashNullLastName);
    assertNotEquals(hashFull, hashNullPassword);
}
}