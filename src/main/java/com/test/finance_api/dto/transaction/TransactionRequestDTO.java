package com.test.finance_api.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TransactionRequestDTO(
        @NotBlank(message = "UserId não pode ser vazio")
        @NotNull(message = "UserId não pode ser nulo")
        String userId,

        double amount,

        LocalDateTime date,

        String description,

        @NotBlank(message = "Category não pode ser vazio")
        @NotNull(message = "UserId não pode ser nulo")
        String category,

        @NotBlank(message = "Account não pode ser vazio")
        @NotNull(message = "UserId não pode ser nulo")
        String account,

        @NotBlank(message = "Type não pode ser vazio")
        @NotNull(message = "UserId não pode ser nulo")
        String type,

        Boolean installment,

        double installmentValue
) {}
