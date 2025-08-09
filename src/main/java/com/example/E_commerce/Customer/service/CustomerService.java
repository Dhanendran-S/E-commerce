package com.example.E_commerce.Customer.service;

import com.example.E_commerce.Customer.assembler.CustomerAssembler;
import com.example.E_commerce.Customer.dto.CustomerRequestDTO;
import com.example.E_commerce.Customer.dto.CustomerResponseDTO;
import com.example.E_commerce.Exception.CustomerNotFoundException;
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

import static com.example.E_commerce.Constants.CommonConstants.C_NOTFOUND;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerAssembler customerAssembler;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, CustomerAssembler customerAssembler) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.customerAssembler = customerAssembler;
    }

    public Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return customerRepository.findAll(pageable).map(CustomerMapper::toResponseDTO);
    }

    public CustomerResponseDTO getCustomerById(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(C_NOTFOUND));
        return customerMapper.toResponseDTO(customer);
    }

    public CustomerResponseDTO saveCustomer(CustomerRequestDTO dto) { //Validation need to be done here
        Customer customer = CustomerMapper.toEntity(dto);
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponseDTO(saved);
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto)
    {
            Optional<Customer> customerOptional = customerRepository.findById(id);
            if(customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                customer.setCName(dto.getCName());
                customer.setCEmail(dto.getCEmail());
                customer.setPhoneNumber(dto.getPhoneNumber());
                customer.setUsername(dto.getUsername());
                customer.setPassword(dto.getPassword());
                return customerMapper.toResponseDTO(customerRepository.save(customer));
            }
            else {
                throw new CustomerNotFoundException(C_NOTFOUND);
            }
    }

    public void deleteCustomer(Long id) {
        if(!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(C_NOTFOUND);
        }
        customerRepository.deleteById(id);
    }

}
