package com.example.E_commerce.CustomerAddress.assembler;

import com.example.E_commerce.Persistance.model.CustomerAddress;
import org.springframework.stereotype.Component;

import com.example.E_commerce.CustomerAddress.controller.CustomerAddressController;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CustomerAddressAssembler implements RepresentationModelAssembler<CustomerAddressResponseDTO, EntityModel<CustomerAddressResponseDTO>> {
    @Override
    public EntityModel<CustomerAddressResponseDTO> toModel(CustomerAddressResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(CustomerAddressController.class).getCustomerAddresses(dto.getCustomerId())).withSelfRel());
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

