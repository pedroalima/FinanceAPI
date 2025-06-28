package com.test.finance_api.dto.transaction;

import java.time.LocalDateTime;

public record TransactionDTO(
        String id,
        String userId,
        double amount,
        LocalDateTime date,
        String description,
        String category,
        String account,
        String type,
        Boolean installment,
        double installmentValue,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
