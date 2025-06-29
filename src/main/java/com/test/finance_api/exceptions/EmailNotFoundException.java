package com.test.finance_api.exceptions;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException() { super("Email não encontrado!");}

    public EmailNotFoundException(String message) {
        super(message);
    }
}
