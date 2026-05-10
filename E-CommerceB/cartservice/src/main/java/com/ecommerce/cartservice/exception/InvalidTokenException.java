package com.ecommerce.cartservice.exception;

// ─── 401 – bad / expired token ────────────────────────────────────────────────
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
