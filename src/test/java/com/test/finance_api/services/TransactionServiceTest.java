package com.test.finance_api.services;

import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
import com.test.finance_api.entity.Transaction;
import com.test.finance_api.exceptions.TransactionNotFoundException;
import com.test.finance_api.exceptions.UserNotFoundException;
import com.test.finance_api.infra.mapper.TransactionMapper;
import com.test.finance_api.repositories.TransactionRepository;
import com.test.finance_api.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void getAllByUserId_EmptyListTransactions() {
        // Dados
        String userId = "user123";
        List<Transaction> emptyList = Collections.emptyList();
        TransactionDTO[] emptyListDTO = new TransactionDTO[0];

        // Mocks
        doNothing().when(this.userValidator).verify(userId);
        when(repository.findAllByUserId(userId)).thenReturn(emptyList);
        when(mapper.transactionListToDTO(emptyList)).thenReturn(emptyListDTO);

        // Execução
        ResponseEntity<TransactionResponseDTO> response = service.getAllByUserId(userId);

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nenhuma transação encontrada para o usuário", response.getBody().message());
        assertEquals(0, response.getBody().transactions().length);
        assertNull(response.getBody().transaction());
    }

    @Test
    void getAllByUserId_ListTransactions() {
        // Dados
        List<Transaction> transactions = List.of(this.createTransaction());
        TransactionDTO dto = mock(TransactionDTO.class);
        TransactionDTO[] dtos = new TransactionDTO[]{dto};

        // Mocks
        doNothing().when(this.userValidator).verify(transactions.getFirst().getUserId());
        when(this.repository.findAllByUserId(transactions.getFirst().getUserId())).thenReturn(transactions);
        when(this.mapper.transactionListToDTO(transactions)).thenReturn(dtos);

        // Execução
        ResponseEntity<TransactionResponseDTO> response = this.service.getAllByUserId(transactions.getFirst().getUserId());

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Todas as transações do usuário", response.getBody().message());
        assertArrayEquals(dtos, response.getBody().transactions());
        assertNull(response.getBody().transaction());
    }

    @Test
    void getAllByUserId_UserNotFoundException() {
        //Dados
        String userId = "userInexistente";

        // Mocks
        when(this.repository.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        when(this.mapper.transactionListToDTO(Collections.emptyList())).thenReturn(new TransactionDTO[0]);

        // Execução e Verificação
        doThrow(new UserNotFoundException("UserId: " + userId + " não encontrado"))
                .when(this.userValidator).verify(userId);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> this.service.getAllByUserId(userId)
        );

        // Verificações
        assertEquals("UserId: " + userId + " não encontrado", exception.getMessage());
    }

    @Test
    public void getById_FoundedTransaction() {
        // Dados
        Transaction transaction = this.createTransaction();
        TransactionDTO transactionDTO = this.createTransactionDTO();

        //Mocks
        when(this.repository.findById(transaction.getId())).thenReturn(java.util.Optional.of(transaction));
        when(this.mapper.transactionToDTO(transaction)).thenReturn(transactionDTO);

        // Execução
        ResponseEntity<TransactionResponseDTO> response = this.service.getById(transaction.getUserId(), transaction.getId());

        // Verificações
        verify(this.userValidator).verify(transaction.getUserId());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TransactionResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Transação encontrada com sucesso!", responseBody.message());
        assertEquals(transactionDTO, responseBody.transaction());
    }

    @Test
    public void getById_TransactionNotFoundException() {
        // Dados
        String userId = "user123";
        String transactionId = "txInexistente";

        // Mocks
        when(this.repository.findById(transactionId)).thenReturn(java.util.Optional.empty());

        // Execução e verificação
        TransactionNotFoundException exception = assertThrows(
                TransactionNotFoundException.class,
                () -> this.service.getById(userId, transactionId)
        );

        // Verificações
        assertTrue(exception.getMessage().contains("TransactionId: " + transactionId + " não encontrado"));
        verify(this.userValidator).verify(userId);
    }

    private Transaction createTransaction() {
        return new Transaction(
                "tx123",
                "user123",
                100.0,
                LocalDateTime.parse("2023-10-01T10:00:00"),
                "Descrição",
                "Categoria",
                "Conta",
                "Tipo",
                false,
                0.0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private TransactionDTO createTransactionDTO() {
        Transaction t = createTransaction();
        return new TransactionDTO(
                t.getId(),
                t.getUserId(),
                t.getAmount(),
                t.getDate(),
                t.getDescription(),
                t.getCategory(),
                t.getAccount(),
                t.getType(),
                t.getInstallment(),
                t.getInstallmentValue(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}