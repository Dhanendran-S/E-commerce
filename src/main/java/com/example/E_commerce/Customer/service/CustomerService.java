package com.example.E_commerce.Customer.service;

import com.example.E_commerce.Customer.assembler.CustomerAssembler;
import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.Exception.CustomerNotFoundException;
import com.example.E_commerce.Exception.ValidationException;
import com.example.E_commerce.Persistance.utils.CustomerMapper;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.example.E_commerce.Constants.CommonConstants.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAssembler customerAssembler;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerAssembler customerAssembler,  CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerAssembler = customerAssembler;
        this.customerMapper = customerMapper;
    }

    public Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return customerRepository.findAll(pageable)
                .map(CustomerAssembler::toResponseDTO);
    }

    public CustomerResponseDTO getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(C_NOTFOUND));
        return customerAssembler.toResponseDTO(customer);
    }

    public CustomerResponseDTO saveCustomer(CustomerRequestDTO dto) {
        if (customerRepository.existsByUsername(dto.getUsername())) {
            throw new ValidationException(USERNAME_EXISTS);
        }
        /*if (customerRepository.existsByCEmail(dto.getCEmail())) {
            throw new ValidationException(EMAIL_EXISTS);
        }*/
        Customer customer = customerMapper.toEntity(dto);
        Customer saved = customerRepository.save(customer);
        return customerAssembler.toResponseDTO(saved);
    }

    public CustomerResponseDTO updateCustomer(UUID id, CustomerRequestDTO dto)
    {
            Optional<Customer> customerOptional = customerRepository.findById(id);
            if(customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                customer.setCName(dto.getCName());
                customer.setCEmail(dto.getCEmail());
                customer.setPhoneNumber(dto.getPhoneNumber());
                customer.setUsername(dto.getUsername());
                customer.setPassword(dto.getPassword());
                return customerAssembler.toResponseDTO(customerRepository.save(customer));
            }
            else {
                throw new CustomerNotFoundException(C_NOTFOUND);
            }
    }

    public void deleteCustomer(UUID id) {
        if(!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(C_NOTFOUND);
        }
        customerRepository.deleteById(id);
    }

}
