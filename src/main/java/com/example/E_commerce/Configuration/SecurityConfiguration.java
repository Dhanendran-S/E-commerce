package com.example.E_commerce.Configuration;

import com.example.E_commerce.Configuration.Filter.JwtFilter;
import com.example.E_commerce.Configuration.Handler.CustomAccessDeniedHandler;
import com.example.E_commerce.Configuration.Service.CustomerDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomerDetailsService userDetailsService;

    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final JwtFilter jwtFilter;

    public SecurityConfiguration(CustomerDetailsService userDetailsService, CustomAccessDeniedHandler accessDeniedHandler, JwtFilter jwtFilter ) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/admin/**").hasRole("ADMIN")

                        // Customers
                        .requestMatchers("/customers/all", "/customers/delete/**").hasRole("ADMIN")
                        .requestMatchers("/customers/my/**", "/customers/update/**").hasAnyRole("ADMIN", "USER")

                        // Customers Address
                        .requestMatchers("/customers-address/my-address/**", "/customers-address/add-address", "/customers-address/update-address/**").hasRole("USER")

                        // Customers Combined Address
                        .requestMatchers("/customer-combined/**").hasRole("USER")

                        // Orders
                        .requestMatchers("/orders/all", "/orders/delete/**").hasRole("ADMIN")
                        .requestMatchers("/orders/create", "/orders/my-order/**", "/orders/delete-my/**").hasAnyRole("ADMIN", "USER")

                        // Products
                        .requestMatchers("/products/add/**", "/products/update/**", "/products/delete/**").hasRole("ADMIN")
                        .requestMatchers("/products/product/**").hasAnyRole("ADMIN", "USER")

                        // Public APIs
                        .requestMatchers(
                              //  "/add-user",
                                "/customers/create", //username, password -->
                                "/products/all",
                                "/home/**",
                                "/kafka/**"
                        ).permitAll()
                        // Default rule
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                       .accessDeniedHandler(accessDeniedHandler) )// custom message for 403
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults()) //For Postman login
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10)); //for production -> strength must be 12
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    //JWT
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
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
