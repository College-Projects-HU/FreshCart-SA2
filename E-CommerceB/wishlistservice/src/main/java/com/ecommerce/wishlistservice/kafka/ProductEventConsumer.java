package com.ecommerce.wishlistservice.kafka;

import com.ecommerce.wishlistservice.repo.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventConsumer {

    private final WishlistRepository wishlistRepository;

    @KafkaListener(topics = "${app.kafka.topics.product-deleted:product.deleted}",
                   groupId = "${spring.kafka.consumer.group-id:wishlist-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onProductDeleted(Map<String, Object> payload) {
        String productId = payload != null && payload.get("productId") != null ? String.valueOf(payload.get("productId")) : null;
        log.info("Received product.deleted event: productId={}", productId);
        if (productId == null || productId.isBlank()) {
            log.warn("Skipping product.deleted event without productId: {}", payload);
            return;
        }
        wishlistRepository.deleteAllByProductId(productId);
        log.info("Removed all wishlist entries for deleted productId={}", productId);
    }
}
