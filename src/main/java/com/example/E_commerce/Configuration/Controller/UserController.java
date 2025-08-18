package com.example.E_commerce.Configuration.Controller;

import com.example.E_commerce.Configuration.Service.UserService;
import com.example.E_commerce.Persistance.model.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String home(HttpServletRequest request) {
        return "E-Commerce Management --> Session ID: " + request.getSession().getId();
    }

    //Session Token
    /*@PostMapping("/login")
    public String login(@RequestBody Users user) {
        return userService.verify(user);
    }*/

    //JWT
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Users user) {
        String token = userService.verify(user);
        Map<String, String> response = new HashMap<>();
        if(!token.equals("fail")) {
            response.put("token", token);
        } else {
            response.put("error", "Invalid username or password");
        }
        return response;
    }

    //For new User
    @PostMapping("/add-user")
    public String addUser(@RequestBody Users user, HttpServletRequest request) {
        userService.addUser(user);

        //Session Token
        HttpSession session = request.getSession();
        session.setAttribute("LoggedInUser", user);

        //JWT

        return "User added and stored sucessfully";
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
}
