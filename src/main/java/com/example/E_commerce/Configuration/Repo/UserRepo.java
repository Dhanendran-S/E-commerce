package com.example.E_commerce.Configuration.Repo;

import com.example.E_commerce.Persistance.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByUsername(String username);
}
