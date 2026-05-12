package com.ecommerce.cartservice.kafka;

import com.ecommerce.cartservice.events.OrderCreatedEvent;
import com.ecommerce.cartservice.exception.ResourceNotFoundException;
import com.ecommerce.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final CartService cartService;

    @KafkaListener(topics = "${app.kafka.topics.order-created:order.created}",
                   groupId = "${spring.kafka.consumer.group-id:cart-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    public void onOrderCreated(Map<String, Object> payload) {
        String orderId = payload != null && payload.get("orderId") != null ? String.valueOf(payload.get("orderId")) : "unknown";
        String userId = payload != null && payload.get("userId") != null ? String.valueOf(payload.get("userId")) : null;
        log.info("Received order.created event: orderId={}, userId={}", orderId, userId);
        if (userId == null || userId.isBlank()) {
            log.warn("Skipping order.created event without userId: {}", payload);
            return;
        }
        try {
            cartService.clearCart(userId);
            log.info("Cart cleared for userId={} after order orderId={}", userId, orderId);
        } catch (ResourceNotFoundException e) {
            log.warn("No cart found for userId={} — already cleared or never existed", userId);
        } catch (Exception e) {
            log.error("Failed to clear cart for userId={}: {}", userId, e.getMessage());
        }
    }
}
