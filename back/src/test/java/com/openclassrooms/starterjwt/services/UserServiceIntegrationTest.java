package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@SpringJUnitConfig
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Laisse H2 gérer automatiquement
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class Config {
        @Bean
        public UserService userService(UserRepository userRepository) {
            return new UserService(userRepository);
        }
    }
 @AfterEach
    void cleanUpDatabase() {
        userRepository.deleteAll(); // 🔁 Nettoyage après chaque test
    }
    @Test
    void testFindById_shouldReturnUser() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setLastName("Test");
        user.setFirstName("Jean");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);

        // Act
        User found = userService.findById(user.getId());

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void testDelete_shouldRemoveUser() {
        // Arrange
        User user = new User();
        user.setEmail("delete@test.com");
        user.setLastName("ToDelete");
        user.setFirstName("Paul");
        user.setPassword("pass");
        user.setAdmin(false);
        user = userRepository.save(user);
        Long id = user.getId();

        // Act
        userService.delete(id);

        // Assert
        Optional<User> deleted = userRepository.findById(id);
        assertThat(deleted).isEmpty();
    }
}
