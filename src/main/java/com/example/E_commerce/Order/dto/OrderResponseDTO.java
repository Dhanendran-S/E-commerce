package com.example.E_commerce.Order.dto;

import com.example.E_commerce.Product.dto.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;

    private List<OrderCustomerDTO> customers;
    private List<ProductResponseDTO> products;
}
