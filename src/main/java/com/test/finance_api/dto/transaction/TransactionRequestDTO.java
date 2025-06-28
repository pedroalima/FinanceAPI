package com.test.finance_api.dto.transaction;

import com.test.finance_api.entity.User;

import java.time.LocalDateTime;

public record TransactionRequestDTO(
        User user,
        double amount,
        LocalDateTime date,
        String description,
        String category,
        String account,
        String type,
        Boolean installment,
        double installmentValue
) {}
