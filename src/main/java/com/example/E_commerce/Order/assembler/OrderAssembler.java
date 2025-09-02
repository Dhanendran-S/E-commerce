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
                linkTo(methodOn(OrderController.class).getOrderById(order.getOrderId(), null)).withSelfRel(),
                linkTo(methodOn(OrderController.class).createOrder(null)).withRel("create-order").withType("POST")
        );
    }

    public static OrderResponseDTO toResponseDTO(Order order, List<ProductQuantityDTO> orderedProducts) {

        List<OrderCustomerDTO> customerDTOList = List.of(
                OrderCustomerDTO.builder()
                        .customerId(order.getCustomer().getCId())
                        .customerName(order.getCustomer().getCName())
                        .build()
        );
        List<ProductResponseDTO> productDTOs = order.getOrderProducts()
                .stream()
                .map(op -> ProductResponseDTO.builder()
                        .id(op.getProduct() != null ? op.getProduct().getPId() : null)
                        .name(op.getProduct() != null ? op.getProduct().getName() : op.getProductName())
                        .description(op.getProduct() != null ? op.getProduct().getDescription() : null)
                        .price(op.getProduct() != null ? op.getProduct().getPrice() : op.getProductPrice())
                        .quantity(op.getQuantity())
                        .build()
                )
                .toList();


        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .customers(customerDTOList)
                .products(productDTOs)
                .build();
    }
}
