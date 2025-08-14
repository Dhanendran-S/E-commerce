package com.example.E_commerce.Configuration.Controller;

import com.example.E_commerce.Configuration.Service.UserService;
import com.example.E_commerce.Persistance.model.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        return "E-Commerce Management --> Session ID: " + request.getSession().getId();
    }

    @PostMapping("/add-user")
    public void addUser(@RequestBody Users user) {
        userService.addUser(user);
    }
}
