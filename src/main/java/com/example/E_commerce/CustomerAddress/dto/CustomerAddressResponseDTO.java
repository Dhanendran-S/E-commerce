package com.example.E_commerce.CustomerAddress.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CustomerAddressResponseDTO {
    @JsonIgnore
    private Long customerId;

    private String streetName;
    private String district;
    private String state;
    private String country;
    private String pincode;
}

