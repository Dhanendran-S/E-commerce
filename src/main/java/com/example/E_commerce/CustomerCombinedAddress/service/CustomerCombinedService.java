package com.example.E_commerce.CustomerCombinedAddress.service;

import com.example.E_commerce.CustomerCombinedAddress.assembler.CustomerCombinedAssembler;
import com.example.E_commerce.CustomerCombinedAddress.dto.CustomerCombinedResponseDTO;
import com.example.E_commerce.Exception.CustomerNotFoundException;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.repository.CustomerAddressRepository;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

import static com.example.E_commerce.Constants.CommonConstants.C_NOTFOUND;


@Service
public class CustomerCombinedService {

    private final CustomerRepository customerRepository;;
    private final CustomerAddressRepository customerAddressRepository;
    private final MessageSource messageSource;

    public CustomerCombinedService(CustomerRepository customerRepository, CustomerAddressRepository customerAddressRepository, MessageSource messageSource) {
        this.customerRepository = customerRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.messageSource = messageSource;
    }

    public CustomerCombinedResponseDTO getCustomerWithAddress(UUID id, Locale locale) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(messageSource.getMessage("customer_not_found", null, locale)));
        return CustomerCombinedAssembler.toResponseDTO(customer);

    }
}
