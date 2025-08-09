package com.example.E_commerce.Persistance.utils;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressRequestDTO;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.model.CustomerAddress;
import org.springframework.stereotype.Component;

@Component
public class CustomerAddressMapper {

    public static CustomerAddress toEntity(CustomerAddressRequestDTO dto) {
        return CustomerAddress.builder()
                .streetName(dto.getStreetName())
                .district(dto.getDistrict())
                .state(dto.getState())
                .country(dto.getCountry())
                .pincode(dto.getPincode())
                .build();
    }

    public static CustomerAddressResponseDTO toResponseDTO(CustomerAddress entity) {
        return CustomerAddressResponseDTO.builder()
                .streetName(entity.getStreetName())
                .district(entity.getDistrict())
                .state(entity.getState())
                .country(entity.getCountry())
                .pincode(entity.getPincode())
                .build();
    }
}
