package com.ecommerce.userservice.exception;

public class PasswordConfirmationException extends RuntimeException {
    public PasswordConfirmationException(String enteredValue) {
        super(enteredValue); // getMessage() returns the entered value for the `value` field
    }
}