package com.ecommerce.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[#?!@$%^&*\\-]).{8,}$",
        message = "Password must be at least 8 characters and contain uppercase, lowercase, digit, and special character"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String rePassword;
}