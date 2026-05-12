package com.productservice.productservice.kafka;

import com.productservice.productservice.repos.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final ProductRepository productRepository;

    @KafkaListener(topics = "${app.kafka.topics.order-created:order.created}",
                   groupId = "${spring.kafka.consumer.group-id:product-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onOrderCreated(Map<String, Object> payload) {
        String orderId = payload != null && payload.get("orderId") != null ? String.valueOf(payload.get("orderId")) : "unknown";
        Object itemsObj = payload != null ? payload.get("items") : null;
        if (!(itemsObj instanceof List<?> items)) {
            log.warn("Skipping order.created event without items list: orderId={}, payload={}", orderId, payload);
            return;
        }

        log.info("Received order.created event: orderId={}, items={}", orderId, items.size());
        for (Object itemObj : items) {
            if (!(itemObj instanceof Map<?, ?> rawItem)) {
                continue;
            }
            Object productIdObj = rawItem.get("productId");
            Object countObj = rawItem.get("count");
            if (productIdObj == null || countObj == null) {
                continue;
            }

            String productId = String.valueOf(productIdObj);
            int count;
            try {
                count = Integer.parseInt(String.valueOf(countObj));
            } catch (NumberFormatException ex) {
                log.warn("Invalid count in order.created item for productId={}: {}", productId, countObj);
                continue;
            }

            productRepository.findById(productId).ifPresentOrElse(product -> {
                int currentQty = product.getQuantity() != null ? product.getQuantity() : 0;
                int newQty = Math.max(0, currentQty - count);
                int currentSold = product.getSold() != null ? product.getSold() : 0;
                int newSold = currentSold + Math.max(0, count);
                product.setQuantity(newQty);
                product.setSold(newSold);
                productRepository.save(product);
                log.info("Updated product inventory for productId={}: quantity {} -> {}, sold {} -> {}",
                        productId, currentQty, newQty, currentSold, newSold);
            }, () -> log.warn("Product not found for stock decrement: productId={}", productId));
        }
    }
}
