package com.example.E_commerce.CustomerAddress.dto;

import lombok.Data;

@Data
public class CustomerAddressResponseDTO {
    private Long id;
    private String streetName;
    private String district;
    private String state;
    private String country;
    private String pincode;
    private String customerName;
}

