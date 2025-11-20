package com.example.orderservice.kafka;

import com.example.orderservice.DTO.ProductCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventConsumer {

    @KafkaListener(
            topics = "product.created",
            groupId = "order-group",
            containerFactory = "productEventKafkaListenerFactory"
    )
    public void consumeProductEvent (ProductCreatedEvent event)
    {
        System.out.println("📥 Received Product Event:");
        System.out.println("ID: " + event.getId());
        System.out.println("Name: " + event.getName());
        System.out.println("Price: " + event.getPrice());
        System.out.println("Stock: " + event.getStock());
    }
}
