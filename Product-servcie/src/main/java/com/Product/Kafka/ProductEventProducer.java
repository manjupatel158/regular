package com.Product.Kafka;

import com.Product.DTO.ProductCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventProducer.class);

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "product.created";

    public void sendProductCreatedEvent(ProductCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
        log.info("📢 Published ProductCreatedEvent to Kafka => {}", event);
    }
}
