package com.example.secondhandmarketwebapp.exception;

public class InvalidUserException extends RuntimeException {
    private final String errorMessage;

    public InvalidUserException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
