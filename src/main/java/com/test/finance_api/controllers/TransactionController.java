package com.test.finance_api.controllers;

import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.TransactionRequestDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
import com.test.finance_api.entity.Transaction;
import com.test.finance_api.repositories.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController()
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionRepository _transactionRepository;

    public TransactionController(TransactionRepository transactionRepository) {
        this._transactionRepository = transactionRepository;
    }

    @PutMapping("/create")
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody TransactionRequestDTO body) {

        Transaction newTransaction = new Transaction();

        newTransaction.setUser(body.user());
        newTransaction.setAmount(body.amount());
        newTransaction.setDate(body.date());
        newTransaction.setDescription(body.description());
        newTransaction.setType(body.type());
        newTransaction.setCategory(body.category());
        newTransaction.setAccount(body.account());
        newTransaction.setInstallment(body.installment());
        newTransaction.setInstallmentValue(body.installmentValue());

        this._transactionRepository.save(newTransaction);

        return ResponseEntity.ok(new TransactionResponseDTO(
                "Transação criada com sucesso!", new TransactionDTO(
                newTransaction.getId(),
                newTransaction.getUser().getId(),
                newTransaction.getAmount(),
                newTransaction.getDate(),
                newTransaction.getDescription(),
                newTransaction.getType(),
                newTransaction.getCategory(),
                newTransaction.getAccount(),
                newTransaction.getInstallment(),
                newTransaction.getInstallmentValue(),
                newTransaction.getCreatedAt(),
                newTransaction.getUpdatedAt()
        )));
    }
}
