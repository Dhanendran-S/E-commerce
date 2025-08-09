package com.example.E_commerce.CustomerCombinedAddress.service;

import com.example.E_commerce.CustomerCombinedAddress.dto.CustomerCombinedResponseDTO;
import com.example.E_commerce.Exception.CustomerNotFoundException;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.repository.CustomerAddressRepository;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import com.example.E_commerce.Persistance.utils.CustomerCombinedMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.E_commerce.Constants.CommonConstants.C_NOTFOUND;


@Service
public class CustomerCombinedService {

    private final CustomerRepository customerRepository;
    private final CustomerCombinedMapper customerCombinedMapper;
    private final CustomerAddressRepository customerAddressRepository;

    public CustomerCombinedService(CustomerRepository customerRepository, CustomerAddressRepository customerAddressRepository, CustomerCombinedMapper customerCombinedMapper) {
        this.customerRepository = customerRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.customerCombinedMapper = customerCombinedMapper;
    }

    public CustomerCombinedResponseDTO getCustomerWithAddress(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(C_NOTFOUND));
        return customerCombinedMapper.toResponseDTO(customer);

    }
}
