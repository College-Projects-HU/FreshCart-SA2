package com.ecommerce.userservice.controllers;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.services.AuthService;
import com.ecommerce.userservice.services.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/signup")
    public ResponseEntity<SignupSuccessResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninSuccessResponse> signin(@Valid @RequestBody SigninRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }

    // ── Password Reset ────────────────────────────────────────────────────────

    @PostMapping("/forgotPasswords")
    public ResponseEntity<SimpleResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(passwordResetService.forgotPassword(request));
    }

    @PostMapping("/verifyResetCode")
    public ResponseEntity<SimpleResponse> verifyResetCode(
            @Valid @RequestBody VerifyResetCodeRequest request) {
        return ResponseEntity.ok(passwordResetService.verifyResetCode(request));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(passwordResetService.resetPassword(request));
    }

    // ── Token Refresh ────────────────────────────────────────────────────────
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            @RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshToken));
    }
}