package com.example.E_commerce.Configuration.Repo;

import com.example.E_commerce.Persistance.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Users getByUsername(String username);
}
