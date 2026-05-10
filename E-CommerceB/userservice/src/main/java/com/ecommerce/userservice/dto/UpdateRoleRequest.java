package com.ecommerce.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateRoleRequest {

    @NotBlank(message = "Role is required")
    @Pattern(
        regexp = "^(guest|user|manager|admin)$",
        message = "Role must be one of: guest, user, manager, admin"
    )
    private String role;
}