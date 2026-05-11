package com.ecommerce.cartservice.kafka;

import com.ecommerce.cartservice.events.OrderCreatedEvent;
import com.ecommerce.cartservice.exception.ResourceNotFoundException;
import com.ecommerce.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final CartService cartService;

    @KafkaListener(topics = "${app.kafka.topics.order-created:order.created}",
                   groupId = "${spring.kafka.consumer.group-id:cart-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received order.created event: orderId={}, userId={}", event.orderId(), event.userId());
        try {
            cartService.clearCart(event.userId());
            log.info("Cart cleared for userId={} after order orderId={}", event.userId(), event.orderId());
        } catch (ResourceNotFoundException e) {
            log.warn("No cart found for userId={} — already cleared or never existed", event.userId());
        } catch (Exception e) {
            log.error("Failed to clear cart for userId={}: {}", event.userId(), e.getMessage());
        }
    }
}
