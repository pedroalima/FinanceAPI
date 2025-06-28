package com.test.finance_api.dto.transaction;

import java.time.LocalDateTime;

public record TransactionResponseDTO(String message, TransactionDTO transaction) {}
