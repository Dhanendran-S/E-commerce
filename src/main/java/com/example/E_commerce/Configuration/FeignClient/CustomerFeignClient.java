//package com.example.E_commerce.Configuration.FeignClient;
//
//import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//import java.util.UUID;
//@FeignClient(
//        name = "customer-service",
//        url = "http://localhost:9090/customers"
//)
//public interface CustomerFeignClient {
//
//    @GetMapping("/my/{customerId}")
//    CustomerResponseDTO getCustomerById(
//            @PathVariable("customerId") UUID customerId,
//            @RequestHeader("Authorization") String token
//    );
//}
