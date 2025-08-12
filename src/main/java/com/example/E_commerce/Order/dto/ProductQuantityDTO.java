package com.example.E_commerce.Order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductQuantityDTO {
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @Min(value = 1, message = "Product quantity must be at least 1 for place the order")
    private int quantity;
}

