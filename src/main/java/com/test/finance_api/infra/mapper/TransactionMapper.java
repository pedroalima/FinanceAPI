package com.test.finance_api.infra.mapper;

import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.CreateTransactionRequestDTO;
import com.test.finance_api.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction transactionRequestDTOToTransaction(CreateTransactionRequestDTO createTransactionRequestDTO);

    TransactionDTO transactionToTransactionDTO(Transaction transaction);

    TransactionDTO[] transactionListToTransactionListDTO(List<Transaction> transactions);
}
