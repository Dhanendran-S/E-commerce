package com.example.E_commerce.Customer.controller;

import com.example.E_commerce.Customer.assembler.CustomerAssembler;
import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.Persistance.utils.CustomerMapper;
import com.example.E_commerce.Customer.service.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

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

    @GetMapping("/my/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable UUID id,
                                               @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        return customerService.getCustomerById(id, locale);
    }

    @PostMapping("/create")
    public EntityModel<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO savedCustomer = customerService.saveCustomer(customerRequestDTO);
        return customerAssembler.toModel(savedCustomer);
    }

    @PutMapping("/update/{id}")
    public EntityModel<CustomerResponseDTO> updateCustomer(@PathVariable UUID id, @Valid @RequestBody CustomerRequestDTO dto,
                                                           @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, dto, locale);
        return customerAssembler.toModel(updatedCustomer);
    }

    @DeleteMapping("/delete/{id}")
    public EntityModel<String> deleteCustomer(@PathVariable UUID id,
                                              @RequestParam(name = "lang", required = false, defaultValue = "en") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        customerService.deleteCustomer(id, locale);
        return EntityModel.of(C_DELETED,
                linkTo(methodOn(CustomerController.class)
                        .getAllCustomers(0, 10, "cId", "asc"))
                        .withRel("all-customers")
                        .withType("GET"));
    }

    /*@GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }*/

}
