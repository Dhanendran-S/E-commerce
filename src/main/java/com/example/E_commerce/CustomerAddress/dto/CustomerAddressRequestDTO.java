package com.example.E_commerce.CustomerAddress.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class CustomerAddressRequestDTO {
    private UUID customerId;

    @NotBlank(message = "Street name is required")
    private String streetName;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "\\d{5,6}", message = "Pincode must be 5 or 6 digits")
    private String pincode;
}