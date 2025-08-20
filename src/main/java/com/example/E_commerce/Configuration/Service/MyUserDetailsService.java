package com.example.E_commerce.Configuration.Service;

import com.example.E_commerce.Configuration.Repo.UserRepo;
import com.example.E_commerce.Persistance.model.Users;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.E_commerce.Constants.CommonConstants.U_NOTFOUND;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo repository;

    public MyUserDetailsService(UserRepo repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if(user == null){
            throw new UsernameNotFoundException(U_NOTFOUND);
        }
        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRole().toUpperCase()))
        ); //ROLE_ADMIN / ROLE_CUSTOMER
    }
}
