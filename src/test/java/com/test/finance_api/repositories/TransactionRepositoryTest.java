package com.test.finance_api.repositories;

import com.test.finance_api.dto.transaction.CreateTransactionRequestDTO;
import com.test.finance_api.entity.Transaction;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {
    @Autowired
    TransactionRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should find all transactions by user ID")
    void findAllByUserIdCase1() {
        CreateTransactionRequestDTO body = new CreateTransactionRequestDTO(
                100.0,
                LocalDateTime.parse("2023-10-01T10:00:00"),
                "Descrição",
                "Categoria",
                "Conta",
                "Tipo",
                false,
                0.0
        );

        createTransaction(body, "user123");

        List<Transaction> transactions = repository.findAllByUserId("user123");

        assertThat(transactions).isNotEmpty();
        assertThat(transactions.getFirst().getUserId()).isEqualTo("user123");
    }

    @Test
    @DisplayName("Should not find all transactions by user ID")
    void findAllByUserIdCase2() {
        List<Transaction> transactions = repository.findAllByUserId("user123");

        assertThat(transactions).isEmpty();
    }

    private Transaction createTransaction(CreateTransactionRequestDTO body, String userId) {
        Transaction newTransaction = new Transaction();
        newTransaction.setUserId(userId);
        newTransaction.setAmount(body.amount());
        newTransaction.setDate(body.date());
        newTransaction.setDescription(body.description());
        newTransaction.setCategory(body.category());
        newTransaction.setAccount(body.account());
        newTransaction.setType(body.type());
        newTransaction.setInstallment(body.installment());
        newTransaction.setInstallmentValue(body.installmentValue());

        entityManager.persist(newTransaction);
        return newTransaction;
    }
}