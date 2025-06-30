package com.test.finance_api.infra.exception;

import com.test.finance_api.exceptions.EmailAlreadyExistsException;
import com.test.finance_api.exceptions.EmailNotFoundException;
import com.test.finance_api.exceptions.InvalidAccessException;
import com.test.finance_api.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            org.springframework.web.bind.MethodArgumentNotValidException exception,
            org.springframework.http.HttpHeaders headers,
            org.springframework.http.HttpStatusCode status,
            org.springframework.web.context.request.WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        for (org.springframework.validation.FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        RestErrorMessage errorMessage = new RestErrorMessage(org.springframework.http.HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    private ResponseEntity<RestErrorMessage> handlerEmailAlreadyExists(EmailAlreadyExistsException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.CONFLICT, exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    private ResponseEntity<RestErrorMessage> handlerEmailNotFound(EmailNotFoundException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(InvalidAccessException.class)
    private ResponseEntity<RestErrorMessage> handlerInvalidAccess(InvalidAccessException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.FORBIDDEN, exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<RestErrorMessage> handlerRuntimeError(RuntimeException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<RestErrorMessage> handlerUserNotFound(UserNotFoundException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
