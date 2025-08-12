package com.example.E_commerce.Product.assembler;


import com.example.E_commerce.Persistance.model.Product;
import com.example.E_commerce.Product.controller.ProductController;
import com.example.E_commerce.Product.dto.ProductRequestDTO;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductAssembler implements RepresentationModelAssembler<ProductResponseDTO, EntityModel<ProductResponseDTO>>
{
    @Override
    public EntityModel<ProductResponseDTO> toModel(ProductResponseDTO product) {
        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class)
                        .getAllProducts(null, 0, 50, "price", "desc"))
                        .withRel("all-products")
                        .expand()
        );
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
