package com.test.finance_api.controllers;

import com.test.finance_api.dto.transaction.CreateTransactionRequestDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
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

    @GetMapping("/all/user-id:{userId}")
    public ResponseEntity<TransactionResponseDTO> getAllByUserId(
            @PathVariable(value = "userId") String userId
    ) {
        return this._transactionService.getAllByUserId(userId);
    }

    @GetMapping("/id:{transactionId}/user-id:{userId}")
    public ResponseEntity<TransactionResponseDTO> getById(
            @PathVariable(value = "transactionId") String transactionId,
            @PathVariable(value = "userId") String userId
    ) {
        return this._transactionService.getById(userId, transactionId);
    }

    @PostMapping("/create/user-id:{userId}")
    public ResponseEntity<TransactionResponseDTO> create(
            @PathVariable(value = "userId") String userId,
            @RequestBody @Valid CreateTransactionRequestDTO body
    ) {
        return this._transactionService.create(userId, body);
    }

    @PutMapping("/update/id:{transactionId}/user-id:{userId}")
    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable(value = "transactionId") String transactionId,
            @PathVariable(value = "userId") String userId,
            @RequestBody @Valid CreateTransactionRequestDTO body
    ) {
        return this._transactionService.update(userId, transactionId, body);
    }
}
