package com.example.E_commerce.Persistance.utils;

import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.Persistance.model.CustomerAddress;
import com.example.E_commerce.Persistance.model.Customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.E_commerce.Constants.CommonConstants.USER;

@Component
public class CustomerMapper {

    private final CustomerAddressMapper customerAddressMapper;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public CustomerMapper(CustomerAddressMapper customerAddressMapper) {
        this.customerAddressMapper = customerAddressMapper;
    }

    public static Customer toEntity(CustomerRequestDTO dto) {
        /*Customer customer = new Customer();
        customer.setCName(dto.getCName());
        customer.setCEmail(dto.getCEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setUsername(dto.getUsername());
        customer.setPassword(dto.getPassword());
        return customer;

        Customer customer = Customer.builder()
                                    .(1)
                                    .("John")
                                    .build();
        */
        return Customer.builder()
                .cName((dto.getCName()))
                .cEmail(dto.getCEmail())
                .phoneNumber(dto.getPhoneNumber())
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .role(USER)
                .build();
    }

    public static CustomerResponseDTO toResponseDTO(Customer customer) {
        /*CustomerResponseDTO dto = new CustomerResponseDTO();
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
        return dto;*/
        return CustomerResponseDTO.builder()
                .cId(customer.getCId())
                .cName(customer.getCName())
                .cEmail(customer.getCEmail())
                .PhoneNumber(customer.getPhoneNumber())
                .username(customer.getUsername())
                .addresses(
                        customer.getAddresses() != null
                                ? customer.getAddresses().stream()
                                                            .map(CustomerAddressMapper::toResponseDTO)
                                                            .toList()
                                : null
                )
                .build();
    }
}
