package com.test.finance_api.validators;

import com.test.finance_api.entity.Transaction;
import com.test.finance_api.exceptions.TransactionNotFoundException;
import com.test.finance_api.repositories.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator {
    private final TransactionRepository _transactionRepository;

    public TransactionValidator(TransactionRepository transactionRepository) {
        this._transactionRepository = transactionRepository;
    }

    public Transaction verifyAndReturn(String transactionId) {
        return this._transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("TransactionId: " + transactionId + " não encontrado"));
    }
}
