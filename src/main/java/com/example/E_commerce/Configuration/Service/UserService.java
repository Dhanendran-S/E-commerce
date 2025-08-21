package com.example.E_commerce.Configuration.Service;

import com.example.E_commerce.Configuration.Repo.UserRepo;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    /*public ResponseEntity<?> verify(Customer user) {
        Customer dbUser = customerRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return jwtService.accessToken(dbUser.getUsername()); //access token
        } else {
            throw new RuntimeException("Invalid password!");
        }
    }*/

    public ResponseEntity<?> login(Customer customer) {
        try{
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword()));
            if (authentication.isAuthenticated()) {
                String accessToken = jwtService.accessToken(customer.getUsername());
                String refreshToken = jwtService.refreshToken(customer.getUsername());
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                return ResponseEntity.ok(tokens);
            } else {
                return ResponseEntity.status(401).body("Authentication Failed");
            }
        }catch (BadCredentialsException e){
            return ResponseEntity.status(401).body("Invalid username or password");
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(403).body("Username not found");
        }catch (Exception e){
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

}
