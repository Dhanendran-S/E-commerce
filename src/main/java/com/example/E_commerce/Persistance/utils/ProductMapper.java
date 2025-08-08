package com.example.E_commerce.Persistance.utils;


import com.example.E_commerce.Persistance.model.Product;
import com.example.E_commerce.Product.dto.ProductRequestDTO;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQty(dto.getQuantity());
        return product;
    }

    public static ProductResponseDTO toResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getPId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getStockQty());
        return dto;
    }
}
