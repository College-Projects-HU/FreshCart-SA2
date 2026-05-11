package com.ecommerce.userservice.services;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.exception.EmailAlreadyExistsException;
import com.ecommerce.userservice.exception.InvalidCredentialsException;
import com.ecommerce.userservice.repos.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import com.ecommerce.userservice.exception.UnauthorizedException;
import com.ecommerce.userservice.events.UserRegisteredEvent;
import com.ecommerce.userservice.kafka.UserEventProducer;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    // ── Signup ────────────────────────────────────────────────────────────────

    public SignupSuccessResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Account Already Exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.user)
                .build();

        User saved = userRepository.save(user);
        userEventProducer.publishUserRegistered(new UserRegisteredEvent(
                saved.getId(), saved.getName(), saved.getEmail(), Instant.now()));
        String token = jwtUtil.generateToken(saved);
        String refreshToken = UUID.randomUUID().toString();

        return SignupSuccessResponse.builder()
                .message("success")
                .user(UserResponse.builder()
                        .id(saved.getId())
                        .name(saved.getName())
                        .email(saved.getEmail())
                        .role(saved.getRole().name())
                        .build())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    // ── Signin ────────────────────────────────────────────────────────────────

    public SigninSuccessResponse signin(SigninRequest request) {

        // 1. Look up user by email — same generic error to prevent user enumeration
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                    new InvalidCredentialsException("Incorrect email or password")
                );

        // 2. Verify raw password against stored BCrypt hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Incorrect email or password");
        }

        // 3. Generate fresh JWT
        String token = jwtUtil.generateToken(user);


        // Generate & persist refresh token
        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(Instant.now().plus(7, ChronoUnit.DAYS)); // e.g. 7 days validity for refresh token
        userRepository.save(user);

        return SigninSuccessResponse.builder()
                .message("success")
                .user(UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    // ── Refresh Access Token ────────────────────────────────────────────────────────────────

    public RefreshTokenResponse refreshAccessToken(String refreshToken) {

        // 1. Look up the user by the refresh token value stored in DB
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token. Please login again."));

        // 2. Check if the refresh token has expired
        if (user.getRefreshTokenExpiry().isBefore(Instant.now())) {
            // Wipe it so it can't be reused even after expiry
            user.setRefreshToken(null);
            user.setRefreshTokenExpiry(null);
            userRepository.save(user);
            throw new UnauthorizedException("Refresh token expired. Please login again.");
        }

        // 3. Everything is valid — issue a fresh access token
        String newAccessToken = jwtUtil.generateToken(user);

        return RefreshTokenResponse.builder()
                .message("success")
                .token(newAccessToken)
                .build();
    }
}
