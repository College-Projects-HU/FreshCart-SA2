package com.ecommerce.userservice.validation;

import com.ecommerce.userservice.dto.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidatorImpl
        implements ConstraintValidator<PasswordMatch, SignupRequest> {

    @Override
    public boolean isValid(SignupRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getRePassword() == null) {
            return true; // handled by @NotBlank on each field
        }

        boolean match = request.getPassword().equals(request.getRePassword());

        if (!match) {
            // Attach the error to the rePassword field specifically
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Password and confirm password do not match"
            )
            .addPropertyNode("rePassword")
            .addConstraintViolation();
        }

        return match;
    }
}