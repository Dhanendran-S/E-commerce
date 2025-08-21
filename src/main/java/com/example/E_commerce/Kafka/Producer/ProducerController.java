package com.example.E_commerce.Kafka.Producer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class ProducerController {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/publish/{message}")
    public String send(@PathVariable String message) {
        producerService.sendMessage(message);
        return "Message sent to kafka: " + message;
    }
}
