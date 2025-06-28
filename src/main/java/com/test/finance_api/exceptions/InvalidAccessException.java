package com.test.finance_api.exceptions;

public class InvalidAccessException extends RuntimeException{

    public InvalidAccessException() { super("Credências inválidas!");}

    public InvalidAccessException(String message) { super(message);}
}
