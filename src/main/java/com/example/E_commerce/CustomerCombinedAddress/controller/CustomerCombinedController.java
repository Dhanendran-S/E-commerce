package com.example.E_commerce.CustomerCombinedAddress.controller;

import com.example.E_commerce.CustomerCombinedAddress.dto.CustomerCombinedResponseDTO;
import com.example.E_commerce.CustomerCombinedAddress.service.CustomerCombinedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer-combined")
public class CustomerCombinedController {

    private final CustomerCombinedService service;

    public CustomerCombinedController(CustomerCombinedService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerCombinedResponseDTO> getCustomerWithAddress(@PathVariable Long id) {
        CustomerCombinedResponseDTO response = service.getCustomerWithAddress(id);
        return ResponseEntity.ok(response);
    }
}
