package com.example.E_commerce.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Long customerId;
    private String username;
    private List<ProductQuantityDTO> products;
    private int orderQuantity;
}
