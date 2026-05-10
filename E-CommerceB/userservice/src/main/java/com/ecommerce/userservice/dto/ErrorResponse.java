package com.ecommerce.userservice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class ErrorResponse {
    private String statusMsg;
    private String message;
    private Map<String, String> errors; // field-level errors (nullable)
}