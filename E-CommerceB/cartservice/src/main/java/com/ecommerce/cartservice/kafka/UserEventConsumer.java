package com.ecommerce.cartservice.kafka;

import com.ecommerce.cartservice.events.UserDeletedEvent;
import com.ecommerce.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final CartRepository cartRepository;

    @KafkaListener(topics = "${app.kafka.topics.user-deleted:user.deleted}",
                   groupId = "${spring.kafka.consumer.group-id:cart-service-group}",
                   containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void onUserDeleted(UserDeletedEvent event) {
        log.info("Received user.deleted event: userId={}", event.userId());
        String cartOwnerId = String.valueOf(event.userId());
        if (cartRepository.existsByCartOwnerId(cartOwnerId)) {
            cartRepository.deleteByCartOwnerId(cartOwnerId);
            log.info("Cart deleted for userId={}", event.userId());
        } else {
            log.info("No cart found for userId={} — nothing to delete", event.userId());
        }
    }
}
