package com.example.E_commerce.Persistance.utils;


import com.example.E_commerce.Persistance.model.Product;
import com.example.E_commerce.Product.dto.ProductRequestDTO;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toEntity(ProductRequestDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .description((dto.getDescription()))
                .price(dto.getPrice())
                .stockQty(dto.getQuantity())
                .build();
    }

    public static ProductResponseDTO toResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getPId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getStockQty())
                .build();
    }
}
