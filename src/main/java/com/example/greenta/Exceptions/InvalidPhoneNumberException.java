package com.example.greenta.Exceptions;

public class InvalidPhoneNumberException extends Exception{
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
