package com.productservice.productservice.kafka;

import com.productservice.productservice.events.ProductDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.product-deleted:product.deleted}")
    private String productDeletedTopic;

    public void publishProductDeleted(ProductDeletedEvent event) {
        log.info("Publishing product.deleted event for productId={}", event.productId());
        kafkaTemplate.send(productDeletedTopic, event.productId(), event);
    }
}
