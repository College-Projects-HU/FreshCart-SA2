package com.ecommerce.cartservice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ErrorResponse {
    private String statusMsg;   // "fail" | "error"
    private String message;
}
