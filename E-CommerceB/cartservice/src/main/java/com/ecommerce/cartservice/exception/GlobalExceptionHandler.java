package com.ecommerce.cartservice.exception;

import com.ecommerce.cartservice.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ─── 404 Not Found ────────────────────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .statusMsg("fail")
                        .message(ex.getMessage())
                        .build());
    }

    // ─── 401 Invalid/Expired Token ────────────────────────────────────────────
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException ex) {
        log.warn("Token error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .statusMsg("fail")
                        .message(ex.getMessage())
                        .build());
    }

    // ─── 401 Spring Security AuthenticationException ──────────────────────────
    // Triggered when the JWT filter doesn't set the SecurityContext
    // (expired token, missing token, etc.)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .statusMsg("fail")
                        .message("Invalid Token. please login again")
                        .build());
    }

    // ─── 401 No authenticated user (IllegalStateException from controller) ─────
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("No authenticated user")) {
            log.warn("Unauthenticated request: {}", ex.getMessage());

            // Check if it could be an expired token situation
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.builder()
                            .statusMsg("fail")
                            .message("Expired Token. please login again")
                            .build());
        }
        log.error("IllegalStateException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .statusMsg("error")
                        .message(ex.getMessage())
                        .build());
    }

    // ─── 403 Forbidden ────────────────────────────────────────────────────────
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .statusMsg("fail")
                        .message("You do not have permission to perform this action")
                        .build());
    }

    // ─── 502 External Service ─────────────────────────────────────────────────
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalService(ExternalServiceException ex) {
        log.error("External service error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(ErrorResponse.builder()
                        .statusMsg("error")
                        .message(ex.getMessage())
                        .build());
    }

    // ─── 400 Validation ───────────────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // ─── 500 Fallback ─────────────────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .statusMsg("error")
                        .message("An unexpected error occurred")
                        .build());
    }
}