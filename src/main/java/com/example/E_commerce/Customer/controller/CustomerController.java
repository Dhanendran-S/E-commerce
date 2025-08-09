package com.example.E_commerce.Customer.controller;

import com.example.E_commerce.Customer.assembler.CustomerAssembler;
import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.Persistance.utils.CustomerMapper;
import com.example.E_commerce.Customer.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import static com.example.E_commerce.Constants.CommonConstants.C_DELETED;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerAssembler customerAssembler;
    private final CustomerMapper customerMapper;
    private final PagedResourcesAssembler<CustomerResponseDTO> pagedResourcesAssembler;

    public CustomerController(CustomerService customerService, CustomerAssembler customerAssembler, CustomerMapper customerMapper,  PagedResourcesAssembler<CustomerResponseDTO> pagedResourcesAssembler) {
        this.customerService = customerService;
        this.customerAssembler = customerAssembler;
        this.customerMapper = customerMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/all")
    public PagedModel<EntityModel<CustomerResponseDTO>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "cId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        Page<CustomerResponseDTO> customers = customerService.getAllCustomers(page, size, sortBy, sortOrder);
        return pagedResourcesAssembler.toModel(customers, customerAssembler);
    }

    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/create")
    public EntityModel<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO savedCustomer = customerService.saveCustomer(customerRequestDTO);
        return customerAssembler.toModel(savedCustomer);
    }

    @PutMapping("/{id}")
    public EntityModel<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, dto);
        return customerAssembler.toModel(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public EntityModel<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return EntityModel.of(C_DELETED,
                linkTo(methodOn(CustomerController.class).getAllCustomers(0, 10, "cId", "asc")).withRel("all-customers"));
    }
}
