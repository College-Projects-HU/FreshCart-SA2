package com.ecommerce.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleResponse {
    private String statusMsg;
    private String message;
}