package com.productservice.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class InvalidIdFormatException extends RuntimeException {
    private final String param;
    private final String value;

    public InvalidIdFormatException(String param, String value) {
        super("Invalid ID format");
        this.param = param;
        this.value = value;
    }

    public String getParam() { return param; }
    public String getValue() { return value; }
}
