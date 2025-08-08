package com.example.E_commerce.Persistance.utils;


import com.example.E_commerce.Order.dto.OrderCustomerDTO;
import com.example.E_commerce.Order.dto.OrderRequestDTO;
import com.example.E_commerce.Order.dto.OrderResponseDTO;
import com.example.E_commerce.Order.dto.ProductQuantityDTO;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.model.Order;
import com.example.E_commerce.Persistance.model.Product;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {

    public static Order toEntity(OrderRequestDTO dto, Customer customer, List<Product> products, BigDecimal totalPrice) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(products);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalPrice);
        order.setStatus("PLACED");
        int totalQuantity = dto.getProducts() // samsung - 2, realme - 2, oppo - 2
                .stream()
                .mapToInt(pq -> pq.getQuantity())
                .sum();
        order.setOrderQuantity(totalQuantity); //total - 6
        return order;
    }

    public static OrderResponseDTO toResponseDTO(Order order, List<ProductQuantityDTO> orderedProducts) {
        List<OrderCustomerDTO> customerDTOList = List.of(
                new OrderCustomerDTO(
                        order.getCustomer().getCId(),
                        order.getCustomer().getUsername(),
                        order.getCustomer().getCName()
                )
        );
        List<ProductResponseDTO> productDTOList = order.getProducts()
                .stream()
                .map(product -> {
                    int qty = 0;
                    if (orderedProducts != null) {
                        qty = orderedProducts
                                .stream()
                                .filter(pq -> pq.getProductId().equals(product.getPId()))
                                .map(ProductQuantityDTO::getQuantity) //Get the quantity for the matching products
                                .findFirst() //Get the first matching product
                                .orElse(0);
                    }
                    return new ProductResponseDTO(
                            product.getPId(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            qty
                    );
                })
                .toList();

        return new OrderResponseDTO(
                order.getOrderId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                customerDTOList,
                productDTOList
        );
    }
}
