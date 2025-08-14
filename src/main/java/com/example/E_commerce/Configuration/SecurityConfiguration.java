package com.example.E_commerce.Configuration;

import com.example.E_commerce.Configuration.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final MyUserDetailsService userDetailsService;

    public SecurityConfiguration(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/admin/**").hasRole("ADMIN")

                        // Customers
                        .requestMatchers("/customers/all", "/customers/delete/**").hasRole("ADMIN")
                        .requestMatchers("/customers/my/**", "/customers/update/**").hasAnyRole("ADMIN", "CUSTOMER")

                        // Orders
                        .requestMatchers("/orders/all", "/orders/delete/**").hasRole("ADMIN")
                        .requestMatchers("/orders/create", "/orders/my-order/**", "/orders/delete-my/**").hasAnyRole("ADMIN", "CUSTOMER")

                        // Public APIs
                        .requestMatchers(
                                "/add-user",
                                "/customers/create",
                                "/products/all"
                        ).permitAll()

                        // Default rule
                        .anyRequest().authenticated()
                )
                //.formLogin(Customizer.withDefaults());
                .httpBasic(Customizer.withDefaults()) //For Postman login
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
    /*@Bean
    public UserDetailsService userDetailsService() {

        //1 Collections
        List<UserDetails> users = new ArrayList<>();
        UserDetails user1 = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("ad123")
                .roles("ADMIN")
                .build();

        UserDetails user2 = User.withDefaultPasswordEncoder()
                .username("user")
                .password("us123")
                .roles("USER")
                .build();
        users.add(user1);
        users.add(user2);
        return new InMemoryUserDetailsManager(users);

        //2 Varargs
        //return new InMemoryUserDetailsManager(user1, user2);
    }*/
}
