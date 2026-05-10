package com.ecommerce.userservice.exception;

public class UserNotFoundException extends RuntimeException {

    private final Long userId;

    public UserNotFoundException(Long userId) {
        super("Invalid ID");
        this.userId = userId;
    }

    public UserNotFoundException(String message) {
        super(message);
        this.userId = null;
    }

    public Long getUserId() {
        return userId;
    }
}