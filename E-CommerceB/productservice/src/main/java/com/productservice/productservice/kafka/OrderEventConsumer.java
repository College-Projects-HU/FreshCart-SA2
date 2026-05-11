package com.productservice.productservice.kafka;

import com.productservice.productservice.events.OrderCreatedEvent;
import com.productservice.productservice.repos.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final ProductRepository productRepository;

    @KafkaListener(topics = "${app.kafka.topics.order-created:order.created}",
                   groupId = "${spring.kafka.consumer.group-id:product-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received order.created event: orderId={}, items={}", event.orderId(), event.items().size());
        if (event.items() == null) return;
        for (OrderCreatedEvent.OrderItemEvent item : event.items()) {
            productRepository.findById(item.productId()).ifPresentOrElse(product -> {
                int currentQty = product.getQuantity() != null ? product.getQuantity() : 0;
                int newQty = Math.max(0, currentQty - item.count());
                product.setQuantity(newQty);
                productRepository.save(product);
                log.info("Decremented stock for productId={}: {} -> {}", item.productId(), currentQty, newQty);
            }, () -> log.warn("Product not found for stock decrement: productId={}", item.productId()));
        }
    }
}
