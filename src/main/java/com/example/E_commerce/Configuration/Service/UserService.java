package com.example.E_commerce.Configuration.Service;

import com.example.E_commerce.Configuration.Repo.UserRepo;
import com.example.E_commerce.Persistance.model.Users;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepo userRepo,  AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void addUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public String verify(Users user) {
        Users dbUser = userRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return jwtService.generateToken(dbUser.getUsername()); //token generation
        } else {
            throw new RuntimeException("Invalid password!");
        }
    }
}
