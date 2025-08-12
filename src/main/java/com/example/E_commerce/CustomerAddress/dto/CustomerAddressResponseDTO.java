package com.example.E_commerce.CustomerAddress.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class CustomerAddressResponseDTO {
    @JsonIgnore
    private UUID customerId;
    private String streetName;
    private String district;
    private String state;
    private String country;
    private String pincode;
}

