package com.test.finance_api.infra.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.test.finance_api.dto.transaction.CreateTransactionRequestDTO;
import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction DTOToTransaction(CreateTransactionRequestDTO dto);

    TransactionDTO transactionToDTO(Transaction transaction);

    TransactionDTO[] transactionListToDTO(List<Transaction> transactions);
}
