package com.example.E_commerce.Persistance.repository;

import com.example.E_commerce.Persistance.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
