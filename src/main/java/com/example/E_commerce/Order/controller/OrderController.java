package com.example.E_commerce.Order.controller;

import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.Order.assembler.OrderAssembler;
import com.example.E_commerce.Order.dto.OrderRequestDTO;
import com.example.E_commerce.Order.dto.OrderResponseDTO;
import com.example.E_commerce.Order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public EntityModel<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderRequestDTO);
        return orderAssembler.toModel(createdOrder);
    }

    @GetMapping("/my-order/{id}")
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

    @DeleteMapping("/delete/{id}")
    public EntityModel<OrderResponseDTO> deleteOrderById(@PathVariable Long id) {
        OrderResponseDTO cancelledOrder = orderService.cancelOrder(id);
        return EntityModel.of(cancelledOrder);
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }


    //Rest Template
    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getCustomerForOrder(@PathVariable UUID id, @RequestHeader("Authorization") String token) {
        return orderService.getCustomerInfo(id, token.replace("Bearer ", ""));
    }

}
