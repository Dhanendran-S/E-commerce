package com.example.E_commerce.Order.service;

import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.Exception.CustomerNotFoundException;
import com.example.E_commerce.Exception.OrderNotFoundException;
import com.example.E_commerce.Exception.ProductNotFoundException;
import com.example.E_commerce.Kafka.Producer.ProducerService;
import com.example.E_commerce.Order.assembler.OrderAssembler;
import com.example.E_commerce.Order.dto.OrderRequestDTO;
import com.example.E_commerce.Order.dto.OrderResponseDTO;
import com.example.E_commerce.Order.dto.ProductQuantityDTO;
import com.example.E_commerce.Persistance.model.*;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import com.example.E_commerce.Persistance.repository.OrderProductRepository;
import com.example.E_commerce.Persistance.repository.OrderRepository;
import com.example.E_commerce.Persistance.repository.ProductRepository;
import com.example.E_commerce.Persistance.utils.OrderMapper;
import com.example.E_commerce.Product.dto.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.example.E_commerce.Constants.CommonConstants.*;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderAssembler orderAssembler;
    private final OrderProductRepository  orderProductRepository;
    private final ProducerService producerService;
    private final RestTemplate restTemplate;
    //private final CustomerFeignClient customerFeignClient;

    private String productServiceUrl = "http://localhost:9090/customers/my/";


    public OrderService(OrderRepository orderRepository, OrderAssembler orderAssembler,  CustomerRepository customerRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository,  ProducerService producerService,  RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.orderAssembler = orderAssembler;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.producerService = producerService;
        this.restTemplate = restTemplate;
        //this.customerFeignClient = customerFeignClient;
    }

    /*public CustomerResponseDTO fetchCustomer(UUID customerId, String token) {
        return customerFeignClient.getCustomerById(customerId, "Bearer " + token);
    }*/

    public ResponseEntity<?> getCustomerInfo(UUID customerId, String token) {
        String url = productServiceUrl + customerId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); //JWT token
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<CustomerResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CustomerResponseDTO.class
        );
        return response;
    }

    //New order
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        if (dto.getCustomerId() == null) {
            throw new CustomerNotFoundException(INVALID_CID);
        }
        if (dto.getProducts() == null || dto.getProducts().isEmpty()) {
            throw new OrderNotFoundException(O_NOTFOUND);
        }

        //To check that customer id is not present or not
        Customer customer = getCustomerOrThrow(dto.getCustomerId());
        List<Product> products = validateAndUpdateStock(dto.getProducts());
        products.forEach(productRepository::save); //Save the products with updated stock qty in the product table

        //Calculate the price for it
        BigDecimal totalPrice = calculateTotalPrice(dto.getProducts());
        Order order = OrderMapper.toEntity(dto, customer, totalPrice);
        Order orderSaved = orderRepository.save(order);

        // Send Kafka message
        String kafkaMessage = "New Order Placed: OrderID = " + orderSaved.getOrderId() + ", CustomerID = " + customer.getCId();
        producerService.sendMessage(kafkaMessage);

        List<OrderProduct> orderProducts = dto.getProducts()
                .stream()
                .map(pq -> {
                    Product product = products.stream()
                            .filter(p -> p.getPId().equals(pq.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new ProductNotFoundException(P_NOTFOUND));

                    OrderProductKey key = new OrderProductKey(orderSaved.getOrderId(), product.getPId());

                    return OrderProduct.builder()
                            .id(key)
                            .order(orderSaved)
                            .product(product)
                            .quantity(pq.getQuantity())
                            .productName(product.getName())
                            .productPrice(product.getPrice())
                            .build();
                })
                .toList();

        orderProductRepository.saveAll(orderProducts);
        orderSaved.setOrderProducts(orderProducts);
        OrderResponseDTO orderResponseDTO = orderAssembler.toResponseDTO(orderSaved, dto.getProducts());
        producerService.sendMessage(orderResponseDTO);
        return orderResponseDTO;
    }

    //Orders by Id
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(O_NOTFOUND));

        List<ProductQuantityDTO> orderedProducts = productRepository.findById(id)
                .stream()
                .map(p -> new ProductQuantityDTO(p.getPId(), p.getStockQty())) //p.getProductId(), p.getQuantity()
                .toList();

        return orderAssembler.toResponseDTO(order, orderedProducts);
    }

    //Get all orders
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders
                .stream()
                .map(order -> orderAssembler.toResponseDTO(order, null))
                .toList();
    }

    //Cancel the order
    public OrderResponseDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(O_NOTFOUND));

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return orderAssembler.toResponseDTO(order, null);
    }

    private Customer getCustomerOrThrow(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(C_NOTFOUND));
    }

    private List<Product> validateAndUpdateStock(List<ProductQuantityDTO> productQuantities) {
        return productQuantities.stream()
                .map(pq -> {
                    Product product = productRepository.findById(pq.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(P_NOTFOUND));

                    if (product.getStockQty() < pq.getQuantity()) {
                        throw new ProductNotFoundException(P_QUANTITY);
                    }
                    product.setStockQty(product.getStockQty() - pq.getQuantity());
                    return productRepository.save(product);
                })
                .toList();
    }

    /*private List<Product> validateAndUpdateStock(List<ProductQuantityDTO> productQuantities) {
        return productQuantities.stream()
                .map(pq -> {
                    String url = productServiceUrl + "/product/" + pq.getProductId(); //http://localhost:9090/products/product/8
                    System.out.println(url);
                    ProductResponseDTO productDto = restTemplate.getForObject(url, ProductResponseDTO.class); // Call ProductService to fetch product
                    if (productDto == null) {
                        throw new ProductNotFoundException(P_NOTFOUND);
                    }
                    if (productDto.getQuantity() < pq.getQuantity()) {
                        throw new ProductNotFoundException(P_QUANTITY + productDto.getName());
                    }
                    productDto.setQuantity(productDto.getQuantity() - pq.getQuantity()); // Reduce stock (simulate update)
                    String putUrl = productServiceUrl + "/update/" + pq.getProductId();
                    restTemplate.put(putUrl, productDto); // Update ProductService

                    // Convert ProductResponseDTO -> Product (your entity)
                    Product product= new Product();
                    product.setPId(productDto.getId());
                    product.setName(productDto.getName());
                    product.setPrice(productDto.getPrice());
                    product.setStockQty(productDto.getQuantity());

                    return product;
                })
                .toList();
    }*/


    private BigDecimal calculateTotalPrice(List<ProductQuantityDTO> dto) {
        return dto.stream() //Mainly works with collections
                .map(pq -> { // It takes each product one by one
                    Product product = productRepository.findById(pq.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(P_NOTFOUND));
                    return product.getPrice().multiply(BigDecimal.valueOf(pq.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); //Like total += price inside for loop
    }



    /*private BigDecimal calculateTotalPrice(List<ProductQuantityDTO> dto) {
        return dto.stream()
                .map(pq -> {
                    String url = productServiceUrl + "/product/" + pq.getProductId();
                    ProductResponseDTO productDto = restTemplate.getForObject(url, ProductResponseDTO.class);
                    if (productDto == null) {
                        throw new ProductNotFoundException(P_NOTFOUND);
                    }
                    return productDto.getPrice().multiply(BigDecimal.valueOf(pq.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }*/


}


