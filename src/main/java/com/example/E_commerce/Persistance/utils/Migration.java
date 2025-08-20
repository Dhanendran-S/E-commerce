package com.example.E_commerce.Persistance.utils;

import com.example.E_commerce.ECommerceApplication;
import com.example.E_commerce.Persistance.model.Customer;
import com.example.E_commerce.Persistance.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Migration implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Migration(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        List<Customer> users = customerRepository.findAll();

        for (Customer user : users) {
            String rawPassword = user.getPassword();

            // Check if password is already hashed (BCrypt hashes start with $2a or $2b)
            if (rawPassword != null &&
                    !rawPassword.startsWith("$2a$") &&
                    !rawPassword.startsWith("$2b$")) {

                String encodedPassword = passwordEncoder.encode(rawPassword);
                user.setPassword(encodedPassword);
                customerRepository.save(user); // ✅ use instance
            }
        }
    }

    // Run Migration alone
    /*public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(ECommerceApplication.class)
                        .profiles("migration-only")
                        .run(args);

        context.close(); // exit after migration
    }*/
}

