package com.example.secondhandmarketwebapp.exception;

public class CheckoutException extends RuntimeException {
    private final String errorMessage;
    public CheckoutException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
