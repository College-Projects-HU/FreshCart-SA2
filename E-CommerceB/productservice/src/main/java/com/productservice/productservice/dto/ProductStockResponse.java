package com.productservice.productservice.dto;

public record ProductStockResponse(
        String productId,
        Integer availableQuantity,
        Double price
) {}

