package com.ecommerce.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupSuccessResponse {
    private String message;
    private UserResponse user;
    private String refreshToken;
    private String token;
}
