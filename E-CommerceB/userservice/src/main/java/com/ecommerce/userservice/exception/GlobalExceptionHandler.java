package com.ecommerce.userservice.exception;

import com.ecommerce.userservice.dto.ChangePasswordErrorResponse;
import com.ecommerce.userservice.dto.ChangePasswordErrorResponse.ErrorDetail;
import com.ecommerce.userservice.dto.ErrorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Signup / Signin ───────────────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage())
        );

        String firstMessage = errors.values().stream()
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.builder()
                .statusMsg("fail")
                .message(firstMessage)
                .errors(errors)
                .build()
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse.builder()
                .statusMsg("fail")
                .message(ex.getMessage())
                .build()
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse.builder()
                .statusMsg("fail")
                .message(ex.getMessage())
                .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.builder()
                .statusMsg("fail")
                .message(ex.getMessage())
                .build()
        );
    }

    // ── Change Password ───────────────────────────────────────────────────────

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse.builder()
                .statusMsg("fail")
                .message(ex.getMessage())
                .build()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ChangePasswordErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ChangePasswordErrorResponse.builder()
                .message("fail")
                .errors(ErrorDetail.builder()
                    .value("changeMyPassword")
                    .msg("Invalid ID")
                    .param("id")
                    .location("params")
                    .build())
                .build()
        );
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ChangePasswordErrorResponse> handleIncorrectPassword(IncorrectPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ChangePasswordErrorResponse.builder()
                .message("fail")
                .errors(ErrorDetail.builder()
                    .value(ex.getMessage())
                    .msg("Incorrect current password")
                    .param("password")
                    .location("body")
                    .build())
                .build()
        );
    }

    @ExceptionHandler(PasswordConfirmationException.class)
    public ResponseEntity<ChangePasswordErrorResponse> handlePasswordConfirmation(PasswordConfirmationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ChangePasswordErrorResponse.builder()
                .message("fail")
                .errors(ErrorDetail.builder()
                    .value(ex.getMessage())
                    .msg("Password confirmation is incorrect")
                    .param("password")
                    .location("body")
                    .build())
                .build()
        );
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.builder()
                .statusMsg("fail")
                .message(ex.getMessage())
                .build()
        );
    }
    // ── Catch-all ─────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.builder()
                .statusMsg("fail")
                .message("An unexpected error occurred")
                .build()
        );
    }
}