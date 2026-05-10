package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.events.OrderCreatedEvent;
import com.ecommerce.orderservice.events.OrderStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.order-created:order.created}")
    private String orderCreatedTopic;

    @Value("${app.kafka.topics.order-status-changed:order.status-changed}")
    private String orderStatusChangedTopic;

    @Async
    @TransactionalEventListener
    public void onCreated(OrderCreatedEvent event) {
        kafkaTemplate.send(orderCreatedTopic, String.valueOf(event.orderId()), event);
    }

    @Async
    @TransactionalEventListener
    public void onStatusChanged(OrderStatusChangedEvent event) {
        kafkaTemplate.send(orderStatusChangedTopic, String.valueOf(event.orderId()), event);
    }
}

