package com.ecommerce.userservice.services;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.exception.*;
import com.ecommerce.userservice.repos.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    // public SigninSuccessResponse changePassword(String tokenHeader, ChangePasswordRequest request) {
    public SigninSuccessResponse changePassword(Long userId,ChangePasswordRequest request) {
        // String token = jwtUtil.extractTokenFromHeader(tokenHeader);
        // Long userId  = jwtUtil.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException(request.getCurrentPassword());
        }

        if (!request.getPassword().equals(request.getRePassword())) {
            throw new PasswordConfirmationException(request.getRePassword());
        }

        // Hash new password AND increment version — old tokens now invalid
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTokenVersion(user.getTokenVersion() + 1);  // ← invalidates all old tokens
        user.setRefreshToken(UUID.randomUUID().toString());  // Invalidate old refresh tokens
        user.setRefreshTokenExpiry(Instant.now().plus(7, ChronoUnit.DAYS)); // expire new refresh token in 7 days

        User saved = userRepository.save(user);
        String newToken = jwtUtil.generateToken(saved);

        return SigninSuccessResponse.builder()
                .message("success")
                .user(UserResponse.builder()
                        .id(saved.getId())
                        .name(saved.getName())
                        .email(saved.getEmail())
                        .role(saved.getRole().name())
                        .build())
                .token(newToken)
                .refreshToken(saved.getRefreshToken())
                .build();
    }
}