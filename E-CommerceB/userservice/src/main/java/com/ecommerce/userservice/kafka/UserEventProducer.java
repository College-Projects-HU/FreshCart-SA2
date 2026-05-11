package com.ecommerce.userservice.kafka;

import com.ecommerce.userservice.events.UserDeletedEvent;
import com.ecommerce.userservice.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.user-registered:user.registered}")
    private String userRegisteredTopic;

    @Value("${app.kafka.topics.user-deleted:user.deleted}")
    private String userDeletedTopic;

    public void publishUserRegistered(UserRegisteredEvent event) {
        log.info("Publishing user.registered event for userId={}", event.userId());
        kafkaTemplate.send(userRegisteredTopic, String.valueOf(event.userId()), event);
    }

    public void publishUserDeleted(UserDeletedEvent event) {
        log.info("Publishing user.deleted event for userId={}", event.userId());
        kafkaTemplate.send(userDeletedTopic, String.valueOf(event.userId()), event);
    }
}
