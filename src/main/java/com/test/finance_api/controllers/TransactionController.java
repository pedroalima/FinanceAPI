package com.test.finance_api.controllers;

import com.test.finance_api.dto.transaction.TransactionRequestDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
import com.test.finance_api.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService _transactionService;

    public TransactionController(TransactionService transactionService) {
        this._transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO body) {
        return this._transactionService.create(body);
    }
}
