package com.ecommerce.cartservice.kafka;

import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.repository.CartItemRepository;
import com.ecommerce.cartservice.repository.CartRepository;
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
public class ProductEventConsumer {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @KafkaListener(topics = "${app.kafka.topics.product-deleted:product.deleted}",
                   groupId = "${spring.kafka.consumer.group-id:cart-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onProductDeleted(Map<String, Object> payload) {
        String productId = payload != null && payload.get("productId") != null ? String.valueOf(payload.get("productId")) : null;
        log.info("Received product.deleted event: productId={}", productId);
        if (productId == null || productId.isBlank()) {
            log.warn("Skipping product.deleted event without productId: {}", payload);
            return;
        }

        List<CartItem> items = cartItemRepository.findAllByProductId(productId);
        if (items.isEmpty()) {
            log.info("No cart items found for deleted productId={}", productId);
            return;
        }
        for (CartItem item : items) {
            Cart cart = item.getCart();
            cart.removeItem(item);
            cartRepository.save(cart);
        }
        log.info("Removed {} cart item(s) for deleted productId={}", items.size(), productId);
    }
}
