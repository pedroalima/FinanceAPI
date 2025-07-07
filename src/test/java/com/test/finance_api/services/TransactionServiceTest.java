package com.test.finance_api.services;

import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
import com.test.finance_api.entity.Transaction;
import com.test.finance_api.exceptions.TransactionNotFoundException;
import com.test.finance_api.exceptions.UserNotFoundException;
import com.test.finance_api.infra.mapper.TransactionMapper;
import com.test.finance_api.repositories.TransactionRepository;
import com.test.finance_api.validators.TransactionValidator;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private UserValidator userValidator;

    @Mock
    private TransactionValidator transactionValidator;

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
                .when(this.userValidator).assertByUserId(userId);

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
        when(this.repository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        when(this.transactionValidator.assertById(transaction.getId())).thenReturn(transaction);
        when(this.mapper.transactionToDTO(transaction)).thenReturn(transactionDTO);

        // Execução
        ResponseEntity<TransactionResponseDTO> response = this.service.getById(transaction.getUserId(), transaction.getId());

        // Verificações
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transação encontrada com sucesso!", response.getBody().message());
        assertEquals(transactionDTO, response.getBody().transaction());
        verify(this.userValidator).assertByUserId(transaction.getUserId());
        verify(this.transactionValidator).assertById(transaction.getId());
        verify(this.mapper).transactionToDTO(transaction);
        verifyNoMoreInteractions(this.userValidator, this.transactionValidator, this.mapper, this.repository);
    }

    @Test
    public void getById_TransactionNotFoundException() {
        // Dados
        String userId = "user123";
        String transactionId = "txInexistente";

        // Mocks
        when(this.repository.findById(transactionId)).thenReturn(java.util.Optional.empty());

        doThrow(new TransactionNotFoundException("TransactionId: " + transactionId + " não encontrado"))
                .when(transactionValidator).assertById(transactionId);

        // Execução e verificação
        TransactionNotFoundException exception = assertThrows(
                TransactionNotFoundException.class,
                () -> this.service.getById(userId, transactionId)
        );

        // Verificações
        assertTrue(exception.getMessage().contains("TransactionId: " + transactionId + " não encontrado"));
        verify(this.userValidator).assertByUserId(userId);
    }

    @Test
    public void delete_TransactionNotFoundException() {
        // Dados
        String userId = "user123";
        String transactionId = "txInexistente";

        // Mocks
        doThrow(new TransactionNotFoundException("TransactionId: " + transactionId + " não encontrado"))
                .when(transactionValidator).assertById(transactionId);

        // Execução e verificação
        TransactionNotFoundException exception = assertThrows(
                TransactionNotFoundException.class,
                () -> this.service.delete(userId, transactionId)
        );

        // Verificações
        assertTrue(exception.getMessage().contains("TransactionId: " + transactionId + " não encontrado"));
        verify(this.userValidator).assertByUserId(userId);
        verify(transactionValidator).assertById(transactionId);
        verifyNoMoreInteractions(transactionValidator, repository, mapper);
    }

    @Test
    public void delete_UserNotFoundException() {
        // Dados
        String userId = "userInexistente";
        String transactionId = "tx123";

        // Mocks
        doThrow(new UserNotFoundException("UserId: " + userId + " não encontrado"))
                .when(this.userValidator).assertByUserId(userId);

        // Execução e verificação
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> this.service.delete(userId, transactionId)
        );

        // Verificações
        assertEquals("UserId: " + userId + " não encontrado", exception.getMessage());
        verify(this.userValidator).assertByUserId(userId);
        verifyNoMoreInteractions(transactionValidator, repository, mapper);
    }

    @Test
    public void delete_TransactionDeleted() {
        // Dados
        String userId = "user123";
        String transactionId = "tx123";

        Transaction transaction = this.createTransaction();
        transaction.setId(transactionId);
        transaction.setUserId(userId);

        TransactionDTO[] updatedDTO = new TransactionDTO[] {
                this.createTransactionDTO()
        };

        List<Transaction> updatedList = List.of(new Transaction());

        // Mocks
        when(transactionValidator.assertById(transactionId)).thenReturn(transaction);
        when(repository.findAllByUserId(userId)).thenReturn(updatedList);
        when(mapper.transactionListToDTO(updatedList)).thenReturn(updatedDTO);

        // Execução
        ResponseEntity<TransactionResponseDTO> response = this.service.delete(userId, transactionId);

        // Verificações
        verify(this.userValidator).assertByUserId(userId);
        verify(this.transactionValidator).assertTransactionBelongsToUser(userId, transactionId, transaction);
        verify(this.repository).delete(transaction);
        verify(this.repository).findAllByUserId(userId);
        verify(this.mapper).transactionListToDTO(updatedList);
        verify(this.transactionValidator).assertById(transactionId);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNull(response.getBody().transaction());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transação deletada com sucesso!", response.getBody().message());
        assertArrayEquals(updatedDTO, response.getBody().transactions());
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