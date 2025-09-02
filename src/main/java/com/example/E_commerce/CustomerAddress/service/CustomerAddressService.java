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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static com.example.E_commerce.Constants.CommonConstants.*;
import static io.micrometer.common.util.StringUtils.isBlank;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository repository;
    private final CustomerAddressMapper mapper;
    private final CustomerRepository customerRepository;
    private final CustomerAddressAssembler assembler;
    private final MessageSource messageSource;

    public CustomerAddressService(CustomerAddressRepository repository, CustomerAddressMapper mapper, CustomerRepository customerRepository, CustomerAddressAssembler assembler,  MessageSource messageSource) {
        this.repository = repository;
        this.mapper = mapper;
        this.customerRepository = customerRepository;
        this.assembler = assembler;
        this.messageSource = messageSource;
    }

    /*public List<CustomerAddressResponseDTO> getAllCustomerAddresses() {
        List<CustomerAddress> addresses = repository.findAll();
        return addresses.stream()
                .map(CustomerAddressMapper::toResponseDTO)
                .toList();
    }*/

    public CustomerAddressResponseDTO getCustomerAddressById(UUID id, Locale locale) {
        CustomerAddress entity = repository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(messageSource.getMessage("customer_address_not_found", null, locale)));
        return assembler.toResponseDTO(entity);
    }

    public CustomerAddressResponseDTO addAddress(CustomerAddressRequestDTO dto, Locale locale) {
        if (dto.getCustomerId() == null) {
            throw new CustomerNotFoundException(INVALID_CID);
        }
        if (isBlank(dto.getStreetName()) || isBlank(dto.getDistrict())
                || isBlank(dto.getPincode()) || isBlank(dto.getCountry())) {
            throw new AddressNotFoundException(messageSource.getMessage("customer_address_not_found", null, locale));
        }
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(messageSource.getMessage("customer_not_found", null, locale)));
        CustomerAddress address = mapper.toEntity(dto);
        address.setCustomer(customer);
        CustomerAddress savedAddress = repository.save(address);
        return assembler.toResponseDTO(savedAddress);
    }

    public CustomerAddressResponseDTO updateAddress(UUID customerId, CustomerAddressRequestDTO dto, Locale locale) {

        Optional<CustomerAddress> optionalAddress = repository.findById(customerId);
        if (optionalAddress.isEmpty()) {
            throw new AddressNotFoundException(messageSource.getMessage("customer_address_not_found", null, locale));
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
