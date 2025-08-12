package com.example.E_commerce.Persistance.utils;


import com.example.E_commerce.Order.dto.OrderCustomerDTO;
import com.example.E_commerce.Order.dto.OrderRequestDTO;
import com.example.E_commerce.Order.dto.OrderResponseDTO;
import com.example.E_commerce.Order.dto.ProductQuantityDTO;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.model.Order;
import com.example.E_commerce.Persistance.model.Product;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {

    public static Order toEntity(OrderRequestDTO dto, Customer customer, BigDecimal totalPrice) {
        return Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .totalAmount(totalPrice)
                .status("PLACED")
                .orderQuantity(
                        dto.getProducts()
                                .stream()
                                .mapToInt(ProductQuantityDTO::getQuantity)
                                .sum()
                )
                .build();
    }

    public static OrderResponseDTO toResponseDTO(Order order, List<ProductQuantityDTO> orderedProducts) {

        List<OrderCustomerDTO> customerDTOList = List.of(
                OrderCustomerDTO.builder()
                        .customerId(order.getCustomer().getCId())
                        //.username(order.getCustomer().getUsername())
                        .customerName(order.getCustomer().getCName())
                        .build()
        );
        List<ProductResponseDTO> productDTOList = order.getOrderProducts()
                .stream()
                .map(op -> ProductResponseDTO.builder()
                        .id(op.getProduct().getPId())
                        .name(op.getProduct().getName())
                        .description(op.getProduct().getDescription())
                        .price(op.getProduct().getPrice())
                        .quantity(op.getQuantity()) // comes directly from DB
                        .build())
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .customers(customerDTOList)
                .products(productDTOList)
                .build();
    }
}
