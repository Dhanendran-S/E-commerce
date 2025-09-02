package com.example.E_commerce.CustomerCombinedAddress.controller;

import com.example.E_commerce.CustomerCombinedAddress.dto.CustomerCombinedResponseDTO;
import com.example.E_commerce.CustomerCombinedAddress.service.CustomerCombinedService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/customer-combined")
public class CustomerCombinedController {

    private final CustomerCombinedService service;

    public CustomerCombinedController(CustomerCombinedService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerCombinedResponseDTO> getCustomerWithAddress(@PathVariable UUID id,
                                                                              @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        CustomerCombinedResponseDTO response = service.getCustomerWithAddress(id, locale);
        return ResponseEntity.ok(response);
    }

    /*@GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }*/
}
