package com.ecommerce.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordErrorResponse {
    private String message;
    private ErrorDetail errors;

    @Data
    @Builder
    public static class ErrorDetail {
        private String value;
        private String msg;
        private String param;
        private String location;
    }
}