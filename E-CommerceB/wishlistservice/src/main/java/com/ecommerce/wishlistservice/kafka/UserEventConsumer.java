package com.ecommerce.wishlistservice.kafka;

import com.ecommerce.wishlistservice.events.UserDeletedEvent;
import com.ecommerce.wishlistservice.repo.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final WishlistRepository wishlistRepository;

    @KafkaListener(topics = "${app.kafka.topics.user-deleted:user.deleted}",
                   groupId = "${spring.kafka.consumer.group-id:wishlist-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onUserDeleted(UserDeletedEvent event) {
        log.info("Received user.deleted event: userId={}", event.userId());
        wishlistRepository.deleteAllByUserId(event.userId());
        log.info("Removed all wishlist entries for deleted userId={}", event.userId());
    }
}
