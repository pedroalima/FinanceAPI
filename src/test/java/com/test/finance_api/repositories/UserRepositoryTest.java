package com.test.finance_api.repositories;

import com.test.finance_api.dto.auth.RegisterRequestDTO;
import com.test.finance_api.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get a user in the database")
    void findByEmailCase1() {
        RegisterRequestDTO body = new RegisterRequestDTO("testuser", "password123", "test@test.com");
        User user = this.createUser(body);

        Optional<User> foundedUser = this.userRepository.findByEmail(user.getEmail());
        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get a user in the database when email does not exist")
    void findByEmailCase2() {
        RegisterRequestDTO body = new RegisterRequestDTO("testuser", "password123", "test@test.com");

        Optional<User> foundedUser = this.userRepository.findByEmail(body.email());
        assertThat(foundedUser.isEmpty()).isTrue();
    }

    private User createUser(RegisterRequestDTO body) {
        User user = new User();
        user.setName(body.username());
        user.setPassword(body.password());
        user.setEmail(body.email());
        entityManager.persist(user);
        return user;
    }
}