package com.example.E_commerce.Order.assembler;

import com.example.E_commerce.Order.controller.OrderController;
import com.example.E_commerce.Order.dto.OrderCustomerDTO;
import com.example.E_commerce.Order.dto.OrderResponseDTO;
import com.example.E_commerce.Order.dto.ProductQuantityDTO;
import com.example.E_commerce.Persistance.model.Order;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements RepresentationModelAssembler<OrderResponseDTO, EntityModel<OrderResponseDTO>> {

    @Override
    public EntityModel<OrderResponseDTO> toModel(OrderResponseDTO order) {
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getOrderId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).createOrder(null)).withRel("create-order")
        );
    }

    public static OrderResponseDTO toResponseDTO(Order order, List<ProductQuantityDTO> orderedProducts) {

        List<OrderCustomerDTO> customerDTOList = List.of(
                OrderCustomerDTO.builder()
                        .customerId(order.getCustomer().getCId())
                        .username(order.getCustomer().getUsername())
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
