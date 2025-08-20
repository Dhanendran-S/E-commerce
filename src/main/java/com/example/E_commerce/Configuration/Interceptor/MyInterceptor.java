package com.example.E_commerce.Configuration.Interceptor;

import com.example.E_commerce.Persistance.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Component
public class MyInterceptor implements HandlerInterceptor {

//    private final OrderRepository orderRepository;
//
//    public MyInterceptor(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 1) Logging part
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "Anonymous";
        System.out.println("User: " + username + " | URL: " + request.getRequestURL() + " | Time: " + LocalDateTime.now());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.println("PostHandle: Controller executed for URL " + request.getRequestURL());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("Interceptor: After request completion");
    }
}