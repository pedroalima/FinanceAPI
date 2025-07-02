package com.test.finance_api.dto.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionResponseDTO(
        String message,
        TransactionDTO transaction,
        TransactionDTO[] transactions
) {}
