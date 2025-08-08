package com.example.E_commerce.Order.service;

import com.example.E_commerce.Order.assembler.OrderAssembler;
import com.example.E_commerce.Order.dto.OrderRequestDTO;
import com.example.E_commerce.Order.dto.OrderResponseDTO;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.model.Order;
import com.example.E_commerce.Persistance.model.Product;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import com.example.E_commerce.Persistance.repository.OrderRepository;
import com.example.E_commerce.Persistance.repository.ProductRepository;
import com.example.E_commerce.Persistance.utils.OrderMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class OrderService {

    private OrderRepository orderRepository;

    private ProductRepository productRepository;

    private CustomerRepository customerRepository;

    private OrderMapper orderMapper;

    private OrderAssembler orderAssembler;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, OrderAssembler orderAssembler,  CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderAssembler = orderAssembler;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        //To check that customer id is not present or not
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        //
        List<Product> products = dto.getProducts()
                .stream()
                .map(pq -> {
                    Product product = productRepository.findById(pq.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + pq.getProductId()));

                    if(product.getStockQty() < pq.getQuantity()) {
                        throw new RuntimeException("Product stock qty less than ordered qty");
                    }
                    product.setStockQty(product.getStockQty() - pq.getQuantity());
                    return product;
                }).toList();

        //Save the products with updated stock qty in the product table
        products.forEach(productRepository::save);

        //Calculate the price for it
        BigDecimal totalPrice = dto.getProducts()
                .stream() //Mainly works with collections
                .map(pq -> { // It takes each product one by one
                    Product product = productRepository.findById(pq.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + pq.getProductId()));
                    return product.getPrice().multiply(BigDecimal.valueOf(pq.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); //Like total += price inside for loop

        Order order = OrderMapper.toEntity(dto, customer, products, totalPrice);
        Order orderSaved = orderRepository.save(order);
        return orderMapper.toResponseDTO(orderSaved, dto.getProducts());
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order =  orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        return orderMapper.toResponseDTO(order, null);
    }

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders
                .stream()
                .map(order -> orderMapper.toResponseDTO(order, null))
                .toList();
    }
}

