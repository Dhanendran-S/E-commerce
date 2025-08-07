package com.example.E_commerce.Customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {
    private String cName;
    private String cEmail;
    private String phoneNumber;
    private String username;
    private String password;
}
