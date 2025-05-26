package com.charity.charityapp.controller;

import com.charity.charityapp.dto.UserDto;
import com.charity.charityapp.enums.Role;
import com.charity.charityapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * AuthController handles login and registration views and submissions.
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/user-login")
    public String userLoginPage() {
        // If user is already logged in, redirect to appropriate dashboard
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/dashboard";
        }
        return "auth/user-login";
    }

//    @GetMapping("/admin-login")
//    public String adminLoginPage() {
//        // If user is already logged in, redirect to appropriate dashboard
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
//            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//                return "redirect:/admin/dashboard";
//            }
//            return "redirect:/dashboard";
//        }
//        return "auth/admin-login";
//    }

    /**
     * Show registration form
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        // If user is already logged in, redirect to appropriate dashboard
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/dashboard";
        }
        
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }

    /**
     * Process registration submission
     */
    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDto userDto, RedirectAttributes redirectAttributes) {
        try {
            log.info("Processing registration for user: {}", userDto.getEmail());
            
            // Validate required fields
            if (userDto.getUsername() == null || userDto.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Username is required");
            }
            if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            if (userDto.getFirstName() == null || userDto.getFirstName().trim().isEmpty()) {
                throw new IllegalArgumentException("First name is required");
            }
            if (userDto.getLastName() == null || userDto.getLastName().trim().isEmpty()) {
                throw new IllegalArgumentException("Last name is required");
            }

            // Ensure new registrations are always USER role
            userDto.setRole(Role.USER);
            
            // Create the user
            userService.create(userDto);
            
            // Add success message and redirect to login
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login with your email and password.");
            return "redirect:/auth/user-login";
            
        } catch (IllegalArgumentException e) {
            log.error("Registration validation failed: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        } catch (Exception e) {
            log.error("Registration failed for user: {}", userDto.getEmail(), e);
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }
}
