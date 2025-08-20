package com.example.E_commerce.Persistance.model;

import com.example.E_commerce.CustomerAddress.dto.CustomerAddressResponseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "users")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cId;
    private String cName;
    private String cEmail;
    private String phoneNumber;
    private String username;
    private String password;
    private String role;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CustomerAddress> addresses;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @PrePersist
    public void assignRole() {
        if (this.role == null) {
            this.role = "USER";
        }
    }
}

