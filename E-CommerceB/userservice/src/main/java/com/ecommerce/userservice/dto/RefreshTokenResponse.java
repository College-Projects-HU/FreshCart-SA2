package com.ecommerce.userservice.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RefreshTokenResponse {
    private String message;
    private String token;
}