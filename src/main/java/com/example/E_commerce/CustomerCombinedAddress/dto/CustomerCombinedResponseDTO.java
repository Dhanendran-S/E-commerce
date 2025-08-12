package com.example.E_commerce.CustomerCombinedAddress.dto;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCombinedResponseDTO {
    private UUID id;
    private String name;
    private String phone;
    private String email;
    private List<CustomerAddressResponseDTO> addresses;
}
