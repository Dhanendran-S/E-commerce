package com.example.E_commerce.Configuration.Filter;

import com.example.E_commerce.Configuration.Service.JWTService;
import com.example.E_commerce.Configuration.Service.CustomerDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter { //Interceptor

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final CustomerDetailsService customerDetailsService;
    private final ApplicationContext applicationContext;

    public JwtFilter(JWTService jwtService, UserDetailsService userDetailsService,  CustomerDetailsService customerDetailsService, ApplicationContext applicationContext) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.customerDetailsService = customerDetailsService;
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5YXNoOTgiLCJpYXQiOjE3NTU2NzMzMDMsImV4cCI6MTc1NTY3MzkwM30.Ns6fjtV1i-SEvrnD0rqJHkaNbNe40dZJqm6x8b6wguo
        String auth = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try {
            if (auth != null && auth.startsWith("Bearer ")) {
                token = auth.substring(7);
                username = jwtService.extractUserName(token);
            }
            // Validate JWT and Set Authentication
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = applicationContext.getBean(CustomerDetailsService.class).loadUserByUsername(username); //User alice, Admin Yash
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expired\"}");
        }
        catch (MalformedJwtException | SignatureException e ) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token\"}");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Something went wrong\"}");
        }
    }
}
