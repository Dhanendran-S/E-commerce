package com.example.E_commerce.CustomerAddress.service;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressRequestDTO;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.model.CustomerAddress;
import com.example.E_commerce.Persistance.repository.CustomerAddressRepository;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import com.example.E_commerce.Persistance.utils.CustomerAddressMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository repository;

    private final CustomerAddressMapper mapper;

    private final CustomerRepository customerRepository;


    public CustomerAddressService(CustomerAddressRepository repository, CustomerAddressMapper mapper, CustomerRepository customerRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.customerRepository = customerRepository;
    }

    /*public List<CustomerAddressResponseDTO> getAllCustomerAddresses() {
        List<CustomerAddress> addresses = repository.findAll();
        return addresses.stream()
                .map(CustomerAddressMapper::toResponseDTO)
                .toList();
    }*/

    public CustomerAddressResponseDTO getCustomerAddressById(Long id) {
        CustomerAddress entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Customer Address with ID " + id + " not found"));
        return mapper.toResponseDTO(entity);
    }

    public CustomerAddressResponseDTO addAddress(CustomerAddressRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        CustomerAddress address = CustomerAddressMapper.toEntity(dto);
        address.setCustomer(customer);
        CustomerAddress savedAddress = repository.save(address);
        return CustomerAddressMapper.toResponseDTO(savedAddress);
    }

    public CustomerAddressResponseDTO updateAddress(Long customerId, CustomerAddressRequestDTO dto) {

        Optional<CustomerAddress> optionalAddress = repository.findById(customerId);
        if (optionalAddress.isEmpty()) {
            throw new RuntimeException("Address not found for Customer ID: " + customerId);
        }

        CustomerAddress address = optionalAddress.get();

        address.setStreetName(dto.getStreetName());
        address.setDistrict(dto.getDistrict());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setPincode(dto.getPincode());

        CustomerAddress updatedAddress = repository.save(address);

        return mapper.toResponseDTO(updatedAddress);
    }

    /*public void deleteAddressById(Long id) {
        CustomerAddress address = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer Address not found with ID: " + id));
        repository.delete(address);
    }*/
}
