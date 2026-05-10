package com.productservice.productservice.util;

import com.productservice.productservice.exception.InvalidIdFormatException;

public class IdValidator {

    // MongoDB ObjectId is 24 hex characters
    private static final java.util.regex.Pattern OBJECT_ID_PATTERN =
            java.util.regex.Pattern.compile("^[a-fA-F0-9]{24}$");

    public static void validate(String id, String paramName) {
        if (id == null || !OBJECT_ID_PATTERN.matcher(id).matches()) {
            System.out.println("Invalid ID format: " + id);
            throw new InvalidIdFormatException(paramName, id);
        }
    }
}
