package com.example.E_commerce.CustomerAddress.assembler;

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
                linkTo(methodOn(CustomerAddressController.class).getCustomerAddresses(dto.getId())).withSelfRel());
    }
}

