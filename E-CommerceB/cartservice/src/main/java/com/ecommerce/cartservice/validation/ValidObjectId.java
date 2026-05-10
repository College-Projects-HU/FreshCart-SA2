package com.ecommerce.cartservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that a String looks like a MongoDB ObjectId (24 hex chars).
 * Use on productId fields to catch malformed IDs before hitting ProductService.
 *
 * Example: @ValidObjectId private String productId;
 */
@Documented
@Constraint(validatedBy = ObjectIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidObjectId {

    String message() default "Invalid product ID format — must be a 24-character hex string";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
