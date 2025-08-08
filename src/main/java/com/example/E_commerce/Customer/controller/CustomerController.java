package com.example.E_commerce.Customer.controller;

import com.example.E_commerce.Customer.assembler.CustomerAssembler;
import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.Customer.dto.ResponseMessage;
import com.example.E_commerce.Persistance.utils.CustomerMapper;
import com.example.E_commerce.Customer.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/customers")
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAssembler customerAssembler;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PagedResourcesAssembler<CustomerResponseDTO> pagedResourcesAssembler;

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
    public EntityModel<ResponseMessage> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        ResponseMessage response = new ResponseMessage("Customer deleted successfully");
        return EntityModel.of(response,
                linkTo(methodOn(CustomerController.class).getAllCustomers(0, 10, "cId", "asc")).withRel("all-customers"));
    }
}
