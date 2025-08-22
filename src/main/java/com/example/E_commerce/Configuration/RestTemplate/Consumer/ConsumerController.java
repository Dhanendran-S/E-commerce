package com.example.E_commerce.Configuration.RestTemplate.Consumer;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class ConsumerController {

    private final ConsumerServices consumerServices;

    public ConsumerController(ConsumerServices consumerServices) {
        this.consumerServices = consumerServices;
    }

    @GetMapping("/call-message")
    public String getMessageFromProvider() {
        return consumerServices.callProviderService();
    }
}
