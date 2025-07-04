package com.test.finance_api.services;

import com.test.finance_api.dto.user.GetAllUserResponse;
import com.test.finance_api.entity.User;
import com.test.finance_api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return all users successfully")
    void getAllUsersCase1() {
        User user = new User(
                "1",
                "John Doe",
                "test@test.com",
                "password123",
                LocalDateTime.parse("2023-01-01T00:00:00"),
                LocalDateTime.parse("2023-01-01T00:00:00")
        );
        when(userRepository.findAll()).thenReturn(List.of(user));


        ResponseEntity<GetAllUserResponse> response = userService.getAllUsers();

        assertThat(response.getBody().message()).isEqualTo("Todas as transações recebidas com sucesso");
        assertThat(response.getBody().users()).hasSize(1);
    }

    @Test
    @DisplayName("Should return empty list when no users found")
    void getAllUsersCase2() {
        when(userRepository.findAll()).thenReturn(List.of());

        ResponseEntity<GetAllUserResponse> response = userService.getAllUsers();

        assertThat(response.getBody().message()).isEqualTo("Nenhum usuário encontrado");
        assertThat(response.getBody().users()).isEmpty();
    }
}