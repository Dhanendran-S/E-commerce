package com.example.E_commerce.Customer.dto;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private UUID cId;
    private String cName;
    private String cEmail;
    private String PhoneNumber;
    private String username;
    private List<CustomerAddressResponseDTO> addresses;
}
