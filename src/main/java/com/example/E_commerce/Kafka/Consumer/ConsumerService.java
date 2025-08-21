package com.example.E_commerce.Kafka.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @KafkaListener(topics = "ORDERS", groupId = "ecommerce-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}
