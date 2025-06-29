package com.test.finance_api.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("UserId não encontrado");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
