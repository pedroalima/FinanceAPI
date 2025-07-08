package com.test.finance_api.validators;

import com.test.finance_api.entity.Transaction;
import com.test.finance_api.exceptions.TransactionNotBelongsToUserException;
import com.test.finance_api.exceptions.TransactionNotFoundException;
import com.test.finance_api.repositories.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator {
    private final TransactionRepository _transactionRepository;

    public TransactionValidator(TransactionRepository transactionRepository) {
        this._transactionRepository = transactionRepository;
    }

    public Transaction assertById(String transactionId) {
        return this._transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("TransactionId: " + transactionId + " não encontrado"));
    }

    public void assertTransactionBelongsToUser(String userId, String transactionId, Transaction transaction) {
        if (!transaction.getUserId().equals(userId))
            throw new TransactionNotBelongsToUserException("TransactionId: " + transactionId + " não pertence ao usuário: " + userId);
    }
}
