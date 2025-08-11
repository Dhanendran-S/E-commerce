package com.example.E_commerce.Persistance.repository;

import com.example.E_commerce.Persistance.model.OrderProduct;
import com.example.E_commerce.Persistance.model.OrderProductKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey> {
    //List<OrderProduct> findByOrder_OrderId(Long orderId);
}

