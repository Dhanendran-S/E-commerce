package com.example.E_commerce.CustomerCombinedAddress.service;

import com.example.E_commerce.CustomerCombinedAddress.dto.CustomerCombinedResponseDTO;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.repository.CustomerAddressRepository;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import com.example.E_commerce.Persistance.utils.CustomerCombinedMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerCombinedService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private final CustomerCombinedMapper customerCombinedMapper;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    public CustomerCombinedResponseDTO getCustomerWithAddress(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerCombinedMapper.toResponseDTO(customer);

    }
}
