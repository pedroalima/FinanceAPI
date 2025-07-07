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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    void getAllUsers_ListUsers() {
        // Dados
        User user = this.createUser();
        User[] dtos = new User[]{user};

        // Mocks
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Execução
        ResponseEntity<GetAllUserResponse> response = userService.getAllUsers();

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Todas as transações recebidas com sucesso", response.getBody().message());
        assertNotNull(response.getBody());
        assertArrayEquals(dtos, response.getBody().users().toArray());
    }

    @Test
    void getAllUsers_EmptyListUsers() {
        // Mocks
        when(userRepository.findAll()).thenReturn(List.of());

        // Execução
        ResponseEntity<GetAllUserResponse> response = userService.getAllUsers();

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Nenhum usuário encontrado", response.getBody().message());
        assertArrayEquals(new User[0], response.getBody().users().toArray());
    }

    private User createUser() {
        return new User(
                "user123",
                "John Doe",
                "test@test.com",
                "password123",
                LocalDateTime.parse("2023-01-01T00:00:00"),
                LocalDateTime.parse("2023-01-01T00:00:00")
        );
    }
}