package com.example.greenta.Exceptions;

public class TooManyLoginAttemptsException extends Exception {
    public TooManyLoginAttemptsException(String message) {
        super(message);
    }
}
