package com.banking.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(	
            AccountNotFoundException ex) {

        return ResponseEntity.status(404)
                             .body(ex.getMessage());
    }
    
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalance(	
    		InsufficientBalanceException ex) {

        return ResponseEntity.badRequest()
                             .body(ex.getMessage());
    }
    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmount(	
    		InvalidAmountException ex) {

        return ResponseEntity.badRequest()
                             .body(ex.getMessage());
    }
}