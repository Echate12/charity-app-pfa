package com.charity.charityapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // If user is authenticated, redirect based on role
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return "redirect:" + (isAdmin ? "/admin/dashboard" : "/dashboard");
        }
        // If not authenticated, redirect to login
        return "redirect:/auth/login";
    }
} 