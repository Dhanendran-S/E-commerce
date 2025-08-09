package com.example.E_commerce.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCustomerDTO {
    private Long customerId;
    private String username;
    private String customerName;
}

