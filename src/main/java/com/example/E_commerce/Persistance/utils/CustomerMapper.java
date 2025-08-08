package com.example.E_commerce.Persistance.utils;

import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.Persistance.model.CustomerAddress;
import com.example.E_commerce.Persistance.utils.CustomerAddressMapper;
import com.example.E_commerce.Persistance.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private final CustomerAddressMapper customerAddressMapper;

    public CustomerMapper(CustomerAddressMapper customerAddressMapper) {
        this.customerAddressMapper = customerAddressMapper;
    }

    public static Customer toEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setCName(dto.getCName());
        customer.setCEmail(dto.getCEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setUsername(dto.getUsername());
        customer.setPassword(dto.getPassword());
        return customer;
    }

    public static CustomerResponseDTO toResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCId(customer.getCId());
        dto.setCName(customer.getCName());
        dto.setCEmail(customer.getCEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setUsername(customer.getUsername());

        if (customer.getAddresses() != null) {
            List<CustomerAddress> addressEntities = customer.getAddresses();
            List<CustomerAddressResponseDTO> addressDTOs = addressEntities
                    .stream()
                    .map(address -> CustomerAddressMapper.toResponseDTO(address))
                    .collect(Collectors.toList());
            dto.setAddresses(addressDTOs);
        }
        return dto;
    }
}
