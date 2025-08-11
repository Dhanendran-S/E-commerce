package com.example.E_commerce.Customer.assembler;

import com.example.E_commerce.Customer.controller.CustomerController;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;

import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.utils.CustomerAddressMapper;
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
