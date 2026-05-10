package com.ecommerce.orderservice.events;

import com.ecommerce.orderservice.model.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusChangedEvent(
        Long orderId,
        Long userId,
        OrderStatus oldStatus,
        OrderStatus newStatus,
        LocalDateTime changedAt
) {}

