package com.example.E_commerce.Persistance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_products")
public class OrderProduct {

    @EmbeddedId
    private OrderProductKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    private int quantity;
    private String productName;
    private BigDecimal productPrice;
}

