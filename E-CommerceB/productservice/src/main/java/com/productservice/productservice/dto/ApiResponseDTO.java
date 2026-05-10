package com.productservice.productservice.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiResponseDTO<T> {

    private  String  status;
    private  String  message;
    private  T       data;

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>("success", message, data);
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>("error", message, null);
    }
}