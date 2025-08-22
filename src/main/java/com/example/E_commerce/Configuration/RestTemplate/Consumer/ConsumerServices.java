package com.example.E_commerce.Configuration.RestTemplate.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumerServices {

    private final RestTemplate restTemplate;

    public ConsumerServices(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callProviderService() {
        String url = "http://localhost:9090/home/message";
        return restTemplate.getForObject(url, String.class);
    }

}
