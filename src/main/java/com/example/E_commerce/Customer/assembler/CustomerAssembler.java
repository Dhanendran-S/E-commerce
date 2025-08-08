package com.example.E_commerce.Customer.assembler;

import com.example.E_commerce.Customer.controller.CustomerController;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerAssembler implements RepresentationModelAssembler<CustomerResponseDTO, EntityModel<CustomerResponseDTO>> {

    @Override
    public EntityModel<CustomerResponseDTO> toModel(CustomerResponseDTO customer) {
        return EntityModel.of(customer,
                linkTo(methodOn(CustomerController.class)
                        .getAllCustomers(0, 10, "cId", "asc"))
                        .withRel("all-customers")
        );
    }
}
