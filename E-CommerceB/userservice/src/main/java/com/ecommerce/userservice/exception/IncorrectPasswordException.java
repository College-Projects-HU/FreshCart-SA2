package com.ecommerce.userservice.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String enteredValue) {
        super(enteredValue); // getMessage() returns the entered value for the `value` field
    }
}