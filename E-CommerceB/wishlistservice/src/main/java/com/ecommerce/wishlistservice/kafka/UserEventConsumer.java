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
public class UserEventConsumer {

    private final WishlistRepository wishlistRepository;

    @KafkaListener(topics = "${app.kafka.topics.user-deleted:user.deleted}",
                   groupId = "${spring.kafka.consumer.group-id:wishlist-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onUserDeleted(Map<String, Object> payload) {
        String userIdValue = payload != null && payload.get("userId") != null ? String.valueOf(payload.get("userId")) : null;
        log.info("Received user.deleted event: userId={}", userIdValue);
        if (userIdValue == null || userIdValue.isBlank()) {
            log.warn("Skipping user.deleted event without userId: {}", payload);
            return;
        }
        try {
            Long userId = Long.parseLong(userIdValue);
            wishlistRepository.deleteAllByUserId(userId);
            log.info("Removed all wishlist entries for deleted userId={}", userId);
        } catch (NumberFormatException ex) {
            log.warn("Skipping user.deleted event with non-numeric userId={}", userIdValue);
        }
    }
}
