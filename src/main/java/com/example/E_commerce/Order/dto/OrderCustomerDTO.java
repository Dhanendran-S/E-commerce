package com.example.E_commerce.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomerDTO {
    private Long customerId;
    private String username;
    private String customerName;
}

