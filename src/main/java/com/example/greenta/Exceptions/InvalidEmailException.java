package com.example.greenta.Exceptions;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}
