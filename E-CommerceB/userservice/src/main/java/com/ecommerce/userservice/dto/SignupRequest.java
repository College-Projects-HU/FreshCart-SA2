package com.ecommerce.userservice.dto;

import com.ecommerce.userservice.validation.PasswordMatch;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@PasswordMatch
public class SignupRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[#?!@$%^&*\\-]).{8,}$",
        message = "Password must be at least 8 characters and contain uppercase, lowercase, digit, and special character"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String rePassword;

    @NotBlank(message = "Phone is required")
    @Pattern(
        regexp = "^01[0125][0-9]{8}$",
        message = "Phone must be a valid Egyptian number (e.g. 010, 011, 012, 015)"
    )
    private String phone;
}