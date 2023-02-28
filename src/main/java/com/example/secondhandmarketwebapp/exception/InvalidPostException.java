package com.example.secondhandmarketwebapp.exception;

public class InvalidPostException extends RuntimeException {
    private final String errorMessage;

    public InvalidPostException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}