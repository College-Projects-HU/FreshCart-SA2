package com.ecommerce.cartservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Checks that a productId string is a valid MongoDB ObjectId:
 *   - Not null / blank
 *   - Exactly 24 characters
 *   - Only hexadecimal characters (0-9, a-f, A-F)
 *
 * This mirrors the error the external API returns:
 *   "Cast to ObjectId failed for value \"6428ebc6dc1175abc65ca0b\" (type string)..."
 * — the example ID is only 23 chars, which is what triggers the error.
 */
public class ObjectIdValidator implements ConstraintValidator<ValidObjectId, String> {

    private static final java.util.regex.Pattern OBJECT_ID_PATTERN =
            java.util.regex.Pattern.compile("^[a-fA-F0-9]{24}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return OBJECT_ID_PATTERN.matcher(value).matches();
    }
}
