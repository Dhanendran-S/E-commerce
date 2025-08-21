package com.example.E_commerce.Kafka.Producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "ORDERS";

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate,  ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    //Methods to send message
    /*public void sendMessage(String message) {
        System.out.println("Sending message to Kafka: " + message);
        kafkaTemplate.send(TOPIC, message);
    }*/

    public void sendMessage(Object messageObj) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(messageObj);
            System.out.println("Sending JSON message: " + jsonMessage);
            kafkaTemplate.send(TOPIC, jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
