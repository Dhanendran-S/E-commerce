package com.example.E_commerce.CustomerAddress.service;

import com.example.E_commerce.CustomerAddress.assembler.CustomerAddressAssembler;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressRequestDTO;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.Exception.AddressNotFoundException;
import com.example.E_commerce.Exception.CustomerNotFoundException;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.model.CustomerAddress;
import com.example.E_commerce.Persistance.repository.CustomerAddressRepository;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import com.example.E_commerce.Persistance.utils.CustomerAddressMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.E_commerce.Constants.CommonConstants.*;
import static io.micrometer.common.util.StringUtils.isBlank;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository repository;
    private final CustomerAddressMapper mapper;
    private final CustomerRepository customerRepository;
    private final CustomerAddressAssembler assembler;

    public CustomerAddressService(CustomerAddressRepository repository, CustomerAddressMapper mapper, CustomerRepository customerRepository, CustomerAddressAssembler assembler) {
        this.repository = repository;
        this.mapper = mapper;
        this.customerRepository = customerRepository;
        this.assembler = assembler;
    }

    /*public List<CustomerAddressResponseDTO> getAllCustomerAddresses() {
        List<CustomerAddress> addresses = repository.findAll();
        return addresses.stream()
                .map(CustomerAddressMapper::toResponseDTO)
                .toList();
    }*/

    public CustomerAddressResponseDTO getCustomerAddressById(Long id) {
        CustomerAddress entity = repository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(C_ADDRESS_NOT_FOUND));
        return assembler.toResponseDTO(entity);
    }

    public CustomerAddressResponseDTO addAddress(CustomerAddressRequestDTO dto) {
        if (dto.getCustomerId() == null || dto.getCustomerId() <= 0) {
            throw new CustomerNotFoundException(INVALID_CID);
        }
        if (isBlank(dto.getStreetName()) || isBlank(dto.getDistrict())
                || isBlank(dto.getPincode()) || isBlank(dto.getCountry())) {
            throw new AddressNotFoundException(C_ADDRESS_EMPTY);
        }
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(C_NOTFOUND));
        CustomerAddress address = mapper.toEntity(dto);
        address.setCustomer(customer);
        CustomerAddress savedAddress = repository.save(address);
        return assembler.toResponseDTO(savedAddress);
    }

    public CustomerAddressResponseDTO updateAddress(Long customerId, CustomerAddressRequestDTO dto) {

        Optional<CustomerAddress> optionalAddress = repository.findById(customerId);
        if (optionalAddress.isEmpty()) {
            throw new AddressNotFoundException(C_ADDRESS_NOT_FOUND);
        }
        CustomerAddress address = optionalAddress.get();
        address.setStreetName(dto.getStreetName());
        address.setDistrict(dto.getDistrict());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setPincode(dto.getPincode());
        CustomerAddress updatedAddress = repository.save(address);
        return assembler.toResponseDTO(updatedAddress);
    }

    /*public void deleteAddressById(Long id) {
        CustomerAddress address = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer Address not found with ID: " + id));
        repository.delete(address);
    }*/
}
