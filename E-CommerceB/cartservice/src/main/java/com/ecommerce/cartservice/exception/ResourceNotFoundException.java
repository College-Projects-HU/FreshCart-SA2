package com.ecommerce.cartservice.exception;

// ─── 404 – resource not found ─────────────────────────────────────────────────
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
