package com.example.E_commerce.CustomerAddress.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerAddressRequestDTO {
    private Long customerId;
    private String streetName;
    private String district;
    private String state;
    private String country;
    private String pincode;
}