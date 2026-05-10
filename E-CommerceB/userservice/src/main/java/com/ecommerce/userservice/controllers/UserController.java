package com.ecommerce.userservice.controllers;

import com.ecommerce.userservice.dto.ChangePasswordRequest;
import com.ecommerce.userservice.dto.SigninSuccessResponse;
import com.ecommerce.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/changeMyPassword")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SigninSuccessResponse> changeMyPassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        Long currentUserId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        SigninSuccessResponse response = userService.changePassword(currentUserId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myOrders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getMyOrders() {
        return ResponseEntity.ok(Map.of("message", "Your orders"));
    }
}
