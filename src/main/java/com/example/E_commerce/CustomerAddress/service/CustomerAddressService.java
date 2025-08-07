package com.example.E_commerce.CustomerAddress.service;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressRequestDTO;
import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.model.CustomerAddress;
import com.example.E_commerce.Persistance.repository.CustomerAddressRepository;
import com.example.E_commerce.Persistance.utils.CustomerAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository repository;

    private final CustomerAddressMapper mapper;

    @Autowired
    public CustomerAddressService(CustomerAddressRepository repository, CustomerAddressMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
        CustomerAddress entity = CustomerAddressMapper.toEntity(dto);
        CustomerAddress saved = repository.save(entity);
        return CustomerAddressMapper.toResponseDTO(saved);
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
