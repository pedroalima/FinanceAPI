package com.test.finance_api.exceptions;

public class TransactionNotBelongsToUserException extends RuntimeException {
    public TransactionNotBelongsToUserException(String message) {
        super(message);
    }

    public TransactionNotBelongsToUserException() {
        super("Transação não pertence ao usuário");
    }
}
