package com.example.E_commerce.Order.controller;

import com.example.E_commerce.Order.assembler.OrderAssembler;
import com.example.E_commerce.Order.dto.OrderRequestDTO;
import com.example.E_commerce.Order.dto.OrderResponseDTO;
import com.example.E_commerce.Order.service.OrderService;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderAssembler orderAssembler;

    public OrderController(OrderService orderService, OrderAssembler orderAssembler) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
    }

    @PostMapping("/create")
    public EntityModel<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderRequestDTO);
        return orderAssembler.toModel(createdOrder);
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/all")
    public List<EntityModel<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orderResponses = orderService.getAllOrders();
        return orderResponses
                .stream()
                .map(orderAssembler::toModel)
                .toList();
    }
}
