package com.ecommerce.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyResetCodeRequest {

    @NotBlank(message = "Reset code is required")
    private String resetCode;
}