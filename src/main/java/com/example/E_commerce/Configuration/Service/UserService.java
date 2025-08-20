package com.example.E_commerce.Configuration.Service;

import com.example.E_commerce.Configuration.Repo.UserRepo;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CustomerRepository customerRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(AuthenticationManager authenticationManager, JWTService jwtService,  CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
    }

    /*public void addUser(Customer user) {
        user.setPassword(encoder.encode(user.getPassword()));
        customerRepository.save(user);
    }*/

    public String verify(Customer user) {
        Customer dbUser = customerRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return jwtService.generateToken(dbUser.getUsername()); //token generation
        } else {
            throw new RuntimeException("Invalid password!");
        }
    }
}
