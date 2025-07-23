package com.credit.exception;

public class CreditoNotFoundException extends RuntimeException {
    public CreditoNotFoundException(String message) {
        super(message);
    }
}