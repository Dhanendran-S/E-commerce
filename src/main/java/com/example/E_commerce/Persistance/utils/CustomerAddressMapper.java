package com.example.E_commerce.Persistance.utils;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressRequestDTO;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.model.CustomerAddress;
import org.springframework.stereotype.Component;

@Component
public class CustomerAddressMapper {

    public static CustomerAddress toEntity(CustomerAddressRequestDTO dto) {
        CustomerAddress address = new CustomerAddress();
        address.setStreetName(dto.getStreetName());
        address.setDistrict(dto.getDistrict());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setPincode(dto.getPincode());
        return address;
    }

    public static CustomerAddressResponseDTO toResponseDTO(CustomerAddress entity) {
        CustomerAddressResponseDTO dto = new CustomerAddressResponseDTO();
        dto.setStreetName(entity.getStreetName());
        dto.setDistrict(entity.getDistrict());
        dto.setState(entity.getState());
        dto.setCountry(entity.getCountry());
        dto.setPincode(entity.getPincode());
        return dto;
    }

}
