package com.charity.charityapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String rootRedirect() {
        // if you wanted to detect already‐logged‐in users you could inject Authentication here,
        // but most apps just send everyone to the login form:
        return "redirect:/auth/login";
    }
}
