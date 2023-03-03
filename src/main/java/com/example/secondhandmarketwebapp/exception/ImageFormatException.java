package com.example.secondhandmarketwebapp.exception;

public class ImageFormatException extends Throwable {
    private final String errorMessage;

    public ImageFormatException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
