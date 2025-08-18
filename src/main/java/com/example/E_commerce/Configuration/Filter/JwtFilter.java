package com.example.E_commerce.Configuration.Filter;

import com.example.E_commerce.Configuration.Service.JWTService;
import com.example.E_commerce.Configuration.Service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final ApplicationContext applicationContext;

    public JwtFilter(JWTService jwtService,  ApplicationContext applicationContext) {
        this.jwtService = jwtService;
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMiIsImlhdCI6MTc1NTUxNjAyOSwiZXhwIjoxNzU1NjI0MDI5fQ.3aYyvWcT2KJp6JPwKFZk9WtJBQqAgEIEtKw_sJ6L43w
        String auth = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (auth != null && auth.startsWith("Bearer ")) {
            token = auth.substring(7);
            username = jwtService.extractUserName(token);
        }
    }
}
