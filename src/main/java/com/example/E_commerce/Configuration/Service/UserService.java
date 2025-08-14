package com.example.E_commerce.Configuration.Service;

import com.example.E_commerce.Configuration.Repo.UserRepo;
import com.example.E_commerce.Persistance.model.Users;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void addUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
