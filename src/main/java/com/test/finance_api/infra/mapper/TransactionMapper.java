package com.test.finance_api.infra.mapper;

import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.TransactionRequestDTO;
import com.test.finance_api.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction transactionRequestDTOToTransaction(TransactionRequestDTO transactionRequestDTO);

    TransactionDTO transactionToTransactionDTO(Transaction transaction);
}
