package com.example.E_commerce.Configuration.Controller;

import com.example.E_commerce.Configuration.Service.JWTService;
import com.example.E_commerce.Configuration.Service.UserService;
import com.example.E_commerce.Persistance.model.Customer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;

    public UserController(UserService userService,  JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping()
    public String home(HttpServletRequest request) {
        return "E-Commerce Management --> Session ID: " + request.getSession().getId();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer customer) {
        return userService.login(customer);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");
        try {
            String username = jwtService.extractUserName(refreshToken);
            String newAccessToken = jwtService.accessToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("access_token", newAccessToken);
            response.put("refresh_token", refreshToken);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(401).body("Invalid or expired refresh token");
        }
    }

    //For logout from the current session
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false -> don’t create new session
        if (session != null) {
            session.invalidate(); // destroys the session
            return "User logged out successfully. Session invalidated.";
        }
        return "No active session found.";
    }


    //JWT
    /*@PostMapping("/login")
    public Map<String, String> login(@RequestBody Customer customer) {
        String accessToken = userService.verify(customer);
        Map<String, String> response = new HashMap<>();
        if (!"fail".equals(accessToken)) {
            response.put("Access token", accessToken);
        } else {
            response.put("error", "Invalid username or password");
        }
        return response;
    }*/

    /*//For new User
    @PostMapping("/add-user")
    public String addUser(@RequestBody Customer customer, HttpServletRequest request) {
        userService.addUser(customer);

        //Session Token
        HttpSession session = request.getSession();
        session.setAttribute("LoggedInUser", customer);
        return "User added and stored sucessfully";
    }*/


}
