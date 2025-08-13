package com.example.E_commerce.Persistance.repository;

import com.example.E_commerce.Persistance.model.Order;
import com.example.E_commerce.Persistance.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
}
