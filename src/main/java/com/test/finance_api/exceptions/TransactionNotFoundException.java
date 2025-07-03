package com.test.finance_api.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException() {
        super("TransactionId não encontrado");
    }
}
