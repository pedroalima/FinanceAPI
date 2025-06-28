package com.test.finance_api.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException() { super("O email já existe!");}

    public EmailAlreadyExistsException(String message) { super(message);}
}
