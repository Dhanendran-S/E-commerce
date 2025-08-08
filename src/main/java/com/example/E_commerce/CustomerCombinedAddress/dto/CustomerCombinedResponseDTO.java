package com.example.E_commerce.CustomerCombinedAddress.dto;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCombinedResponseDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private List<CustomerAddressResponseDTO> addresses;
}
