package com.ecommerce.userservice.services;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.repos.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final int CODE_EXPIRY_MINUTES = 10;

    // ── Step 1: Forgot Password ───────────────────────────────────────────────

    public SimpleResponse forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(
                    "There is no user registered with this email address " + request.getEmail()
                ));

        // Generate 6-digit code
        String resetCode = String.format("%06d", new Random().nextInt(999999));

        // Save code + expiry to DB
        user.setPasswordResetCode(resetCode);
        user.setPasswordResetExpires(LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES));
        user.setPasswordResetVerified(false);
        userRepository.save(user);

        // Send email
        emailService.sendResetCode(user.getEmail(), resetCode);

        return SimpleResponse.builder()
                .statusMsg("success")
                .message("Reset code sent to your email")
                .build();
    }

    // ── Step 2: Verify Reset Code ─────────────────────────────────────────────

    public SimpleResponse verifyResetCode(VerifyResetCodeRequest request) {

        // Find user by reset code
        User user = userRepository.findByPasswordResetCode(request.getResetCode())
                .orElseThrow(() -> new RuntimeException("Reset code is invalid or has expired"));

        // Check expiry
        if (user.getPasswordResetExpires() == null ||
            LocalDateTime.now().isAfter(user.getPasswordResetExpires())) {
            throw new RuntimeException("Reset code is invalid or has expired");
        }

        // Mark as verified
        user.setPasswordResetVerified(true);
        userRepository.save(user);

        return SimpleResponse.builder()
                .statusMsg("Success")
                .message("Reset code verified")
                .build();
    }

    // ── Step 3: Reset Password ────────────────────────────────────────────────

    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(
                    "There is no user with this email address " + request.getEmail()
                ));

        // Must have verified the reset code first
        if (!user.isPasswordResetVerified()) {
            throw new RuntimeException("Reset code has not been verified");
        }

        // Set new password + invalidate old tokens + clear reset fields
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setTokenVersion(user.getTokenVersion() + 1);
        user.setPasswordResetCode(null);
        user.setPasswordResetExpires(null);
        user.setPasswordResetVerified(false);

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved);

        return ResetPasswordResponse.builder()
                .token(token)
                .build();
    }
}