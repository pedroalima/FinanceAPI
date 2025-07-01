package com.test.finance_api.dto.transaction;

public record GetAllTransactionsResponseDTO(String message, TransactionDTO[] transactions) {}
