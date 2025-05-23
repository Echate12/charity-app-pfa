package com.charity.charityapp.controller;

import com.charity.charityapp.dto.UserDto;
import com.charity.charityapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")  // Changed from "auth/login" to "/login"
    public String loginPage(@RequestParam(required=false) boolean error,
                            @RequestParam(required=false) boolean logout,
                            @RequestParam(required=false) boolean registered,
                            Model model) {
        model.addAttribute("loginError", error);
        model.addAttribute("loggedOut", logout);
        model.addAttribute("registered", registered);
        return "auth/login";
    }

    @GetMapping("/register")  // Changed from "auth/register" to "/register"
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute UserDto userDto) {
        userService.create(userDto);
        return "redirect:/auth/login?registered=true";
    }
}