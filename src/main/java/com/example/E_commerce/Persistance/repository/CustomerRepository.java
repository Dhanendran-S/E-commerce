package com.example.E_commerce.Persistance.repository;

import com.example.E_commerce.Persistance.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByUsername(String username); // matches 'username'
    //boolean existsByCEmail(String cEmail);// matches 'cEmail'
    Optional<Customer> findByUsername(String username);
}



