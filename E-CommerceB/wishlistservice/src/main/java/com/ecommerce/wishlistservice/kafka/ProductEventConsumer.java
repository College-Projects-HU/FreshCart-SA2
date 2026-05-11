package com.ecommerce.wishlistservice.kafka;

import com.ecommerce.wishlistservice.events.ProductDeletedEvent;
import com.ecommerce.wishlistservice.repo.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventConsumer {

    private final WishlistRepository wishlistRepository;

    @KafkaListener(topics = "${app.kafka.topics.product-deleted:product.deleted}",
                   groupId = "${spring.kafka.consumer.group-id:wishlist-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onProductDeleted(ProductDeletedEvent event) {
        log.info("Received product.deleted event: productId={}", event.productId());
        wishlistRepository.deleteAllByProductId(event.productId());
        log.info("Removed all wishlist entries for deleted productId={}", event.productId());
    }
}
