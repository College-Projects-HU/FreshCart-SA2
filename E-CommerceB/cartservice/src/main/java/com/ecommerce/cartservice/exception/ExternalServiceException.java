package com.ecommerce.cartservice.exception;

// ─── 502 – upstream ProductService / OrderService error ───────────────────────
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }
}
