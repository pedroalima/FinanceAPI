package com.test.finance_api.services;

import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
import com.test.finance_api.entity.Transaction;
import com.test.finance_api.exceptions.UserNotFoundException;
import com.test.finance_api.infra.mapper.TransactionMapper;
import com.test.finance_api.repositories.TransactionRepository;
import com.test.finance_api.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private UserValidator userValidator;

    @Autowired
    @InjectMocks
    private TransactionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should return empty list when no transactions found for user")
    void getAllByUserIdCase1() {
        String userId = "user123";
        when(repository.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        when(mapper.transactionListToDTO(Collections.emptyList())).thenReturn(new TransactionDTO[0]);

        ResponseEntity<TransactionResponseDTO> response = service.getAllByUserId(userId);

        assertThat(response.getBody().message()).isEqualTo("Nenhuma transação encontrada para o usuário");
        assertThat(response.getBody().transactions()).isEmpty();
    }

    @Test
    @DisplayName("Should return all transactions for user")
    void getAllByUserIdCase2() {
        String userId = "user123";
        Transaction newTransaction = new Transaction();
        newTransaction.setUserId(userId);
        newTransaction.setAmount(100.0);
        newTransaction.setDate(LocalDateTime.parse("2023-10-01T10:00:00"));
        newTransaction.setDescription("Descrição");
        newTransaction.setCategory("Categoria");
        newTransaction.setAccount( "Conta");
        newTransaction.setType("Tipo");
        newTransaction.setInstallment(false);
        newTransaction.setInstallmentValue(0.0);
        List<Transaction> transactions = List.of(newTransaction);

        TransactionDTO dto = mock(TransactionDTO.class);
        TransactionDTO[] dtos = new TransactionDTO[]{dto};

        when(repository.findAllByUserId(userId)).thenReturn(transactions);
        when(mapper.transactionListToDTO(transactions)).thenReturn(dtos);

        ResponseEntity<TransactionResponseDTO> response = service.getAllByUserId(userId);

        assertThat(response.getBody().message()).isEqualTo("Todas as transações do usuário");
        assertThat(response.getBody().transactions()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void getAllByUserIdCase3() throws Exception {
        String userId = "userInexistente";
        doThrow(new UserNotFoundException("UserId: " + userId + " não encontrado"))
                .when(userValidator).verify(userId);

        assertThrows(UserNotFoundException.class, () -> service.getAllByUserId(userId));
    }
}