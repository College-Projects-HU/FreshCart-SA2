package com.ecommerce.cartservice.kafka;

import com.ecommerce.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final CartRepository cartRepository;

    @KafkaListener(topics = "${app.kafka.topics.user-deleted:user.deleted}",
                   groupId = "${spring.kafka.consumer.group-id:cart-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onUserDeleted(Map<String, Object> payload) {
        String userId = payload != null && payload.get("userId") != null ? String.valueOf(payload.get("userId")) : null;
        log.info("Received user.deleted event: userId={}", userId);
        if (userId == null || userId.isBlank()) {
            log.warn("Skipping user.deleted event without userId: {}", payload);
            return;
        }
        String cartOwnerId = userId;
        if (cartRepository.existsByCartOwnerId(cartOwnerId)) {
            cartRepository.deleteByCartOwnerId(cartOwnerId);
            log.info("Cart deleted for userId={}", userId);
        } else {
            log.info("No cart found for userId={} — nothing to delete", userId);
        }
    }
}
