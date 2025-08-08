package com.example.E_commerce.Persistance.utils;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.CustomerCombinedAddress.dto.CustomerCombinedResponseDTO;
import com.example.E_commerce.Persistance.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerCombinedMapper {

    public static CustomerCombinedResponseDTO toResponseDTO(Customer customer) {
        List<CustomerAddressResponseDTO> addressDTOs = customer.getAddresses().stream()
                .map(CustomerAddressMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new CustomerCombinedResponseDTO(
                customer.getCId(),
                customer.getCName(),
                customer.getPhoneNumber(),
                customer.getCEmail(),
                addressDTOs
        );
    }
}
