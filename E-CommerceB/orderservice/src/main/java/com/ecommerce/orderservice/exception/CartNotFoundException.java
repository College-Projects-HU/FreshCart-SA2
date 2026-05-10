package com.ecommerce.orderservice.exception;

// ─── Custom Exceptions ────────────────────────────────────────────────────────

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
