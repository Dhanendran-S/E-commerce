package com.example.E_commerce.Order.service;

import com.example.E_commerce.Persistance.model.Order;
import com.example.E_commerce.Persistance.model.OrderStatus;
import com.example.E_commerce.Persistance.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusScheduler {
    private final OrderRepository orderRepository;

    public OrderStatusScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(cron = "0 15 11 * * ?") //Every
    public void placedToShipping() {
        List<Order> placedOrders = orderRepository.findByStatus(OrderStatus.PLACED);
        placedOrders.forEach(order -> order.setStatus(OrderStatus.SHIPPING));
        orderRepository.saveAll(placedOrders);
    }

    @Scheduled(cron = "0 15 11 * * ?")
    public void shippingToDelivered() {
        List<Order> shippingOrders = orderRepository.findByStatus(OrderStatus.SHIPPING);
        shippingOrders.forEach(order -> order.setStatus(OrderStatus.DELIVERED));
        orderRepository.saveAll(shippingOrders);
    }
}
