package com.ecommerce.orderservice.events;

import com.ecommerce.orderservice.model.OrderStatus;

import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        String productId,
        Integer quantity,
        Double unitPrice,
        Double totalPrice,
        OrderStatus status,
        LocalDateTime createdAt
) {}

