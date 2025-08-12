package com.example.E_commerce.Persistance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerAddressId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "cId")
    private Customer customer;

    private String streetName;
    private String district;
    private String state;
    private String country;
    private String pincode;
}


