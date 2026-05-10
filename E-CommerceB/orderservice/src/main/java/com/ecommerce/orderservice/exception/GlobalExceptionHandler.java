package com.ecommerce.orderservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Translates service-layer exceptions into the standard error envelope:
 * <pre>
 * {
 *   "status":  "error",
 *   "message": "...",
 *   "timestamp": "..."
 * }
 * </pre>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 401 – missing / invalid / expired JWT
    @ExceptionHandler({AuthenticationException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleUnauthorized(RuntimeException ex) {
        log.warn("Unauthorized: {}", ex.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED, "Invalid Token",
                "Your token is missing, expired, or incorrect.");
    }

    // 404 – cart not found or doesn't belong to the caller
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCartNotFound(CartNotFoundException ex) {
        log.warn("Cart not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, "Invalid Cart ID",
                "The cartId in the URL doesn't exist or doesn't belong to you.");
    }

    // 400 – bean-validation failures (missing shippingAddress fields, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", "Validation Error");
        body.put("errors", fieldErrors);
        body.put("timestamp", Instant.now());

        return ResponseEntity.badRequest().body(body);
    }

    // 500 – catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "An unexpected error occurred. Please try again later.");
    }

    // ─── Helper ───────────────────────────────────────────────────────────────
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status,
                                                            String error,
                                                            String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "error");
        body.put("error", error);
        body.put("message", message);
        body.put("timestamp", Instant.now());
        return ResponseEntity.status(status).body(body);
    }
}
