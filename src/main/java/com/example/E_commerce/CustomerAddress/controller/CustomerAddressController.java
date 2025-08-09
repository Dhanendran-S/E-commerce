package com.example.E_commerce.CustomerAddress.controller;

import com.example.E_commerce.CustomerAddress.assembler.CustomerAddressAssembler;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressRequestDTO;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.CustomerAddress.service.CustomerAddressService;

import com.example.E_commerce.Persistance.utils.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customers-address")

public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;
    private final CustomerAddressAssembler customerAddressAssembler;

    public CustomerAddressController(CustomerAddressService customerAddressService, CustomerAddressAssembler customerAddressAssembler) {
        this.customerAddressService = customerAddressService;
        this.customerAddressAssembler = customerAddressAssembler;
    }

    /*@GetMapping("{/all")
    public ResponseEntity<CollectionModel<EntityModel<CustomerAddressResponseDTO>>> getAllCustomerAddress() {
        List<CustomerAddressResponseDTO> addressList = customerAddressService.getAllCustomerAddresses();
        List<EntityModel<CustomerAddressResponseDTO>> models = addressList.stream()
                .map(customerAddressAssembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models));
    }*/


    @GetMapping("/{id}")
    public ResponseEntity<CustomerAddressResponseDTO> getCustomerAddresses(@PathVariable Long id) {
        CustomerAddressResponseDTO dto = customerAddressService.getCustomerAddressById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EntityModel<CustomerAddressResponseDTO>> addAddress(@RequestBody CustomerAddressRequestDTO dto) {
        CustomerAddressResponseDTO savedDto = customerAddressService.addAddress(dto);
        return ResponseEntity.ok(customerAddressAssembler.toModel(savedDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerAddressResponseDTO>> updateAddress(
            @PathVariable Long id,
            @RequestBody CustomerAddressRequestDTO dto
    ) {
        CustomerAddressResponseDTO savedDto = customerAddressService.updateAddress(id, dto);
        return ResponseEntity.ok(customerAddressAssembler.toModel(savedDto));
    }

    /*@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        customerAddressService.deleteAddressById(id);
        return ResponseEntity.ok("Customer address deleted successfully");
    }*/
}
