package com.test.finance_api.controllers;

import com.test.finance_api.dto.transaction.GetAllTransactionsResponseDTO;
import com.test.finance_api.dto.transaction.CreateTransactionRequestDTO;
import com.test.finance_api.dto.transaction.CreateTransactionResponseDTO;
import com.test.finance_api.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService _transactionService;

    public TransactionController(TransactionService transactionService) {
        this._transactionService = transactionService;
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<GetAllTransactionsResponseDTO> getAllByUserId(@PathVariable(value = "userId") String userId) {
        return this._transactionService.getAllByUserId(userId);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateTransactionResponseDTO> create(@RequestBody @Valid CreateTransactionRequestDTO body) {
        return this._transactionService.create(body);
    }
}
