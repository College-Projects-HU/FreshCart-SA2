package com.ecommerce.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninSuccessResponse {
    private String message;
    private UserResponse user;
    private String token;
    private String refreshToken;
}