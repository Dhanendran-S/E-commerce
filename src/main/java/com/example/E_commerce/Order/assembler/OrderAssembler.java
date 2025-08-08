package com.example.E_commerce.Order.assembler;

import com.example.E_commerce.Order.controller.OrderController;
import com.example.E_commerce.Order.dto.OrderResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements RepresentationModelAssembler<OrderResponseDTO, EntityModel<OrderResponseDTO>> {

    @Override
    public EntityModel<OrderResponseDTO> toModel(OrderResponseDTO order) {
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getOrderId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("all-orders"),
                linkTo(methodOn(OrderController.class).createOrder(null)).withRel("create-order")
        );
    }
}
