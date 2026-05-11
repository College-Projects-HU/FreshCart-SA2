package com.productservice.productservice.events;

import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
        String orderId,
        String userId,
        List<OrderItemEvent> items,
        String paymentMethodType,
        Instant createdAt
) {
    public record OrderItemEvent(String productId, int count) {}
}
