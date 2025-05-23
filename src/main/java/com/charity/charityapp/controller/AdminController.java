package com.charity.charityapp.controller;

import com.charity.charityapp.dto.DonationDto;
import com.charity.charityapp.dto.UserDto;
import com.charity.charityapp.service.DonationService;
import com.charity.charityapp.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final DonationService donationService;
    private final UserService     userService;

    public AdminController(DonationService donationService,
                           UserService userService) {
        this.donationService = donationService;
        this.userService     = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalDonations = donationService.countAll();
        int totalUsers      = userService.getAll().size();

        model.addAttribute("totalDonations", totalDonations);
        model.addAttribute("totalUsers", totalUsers);
        return "admin/dashboard";
    }

    @GetMapping("/donations")
    public String allDonations(Model model) {
        List<DonationDto> list = donationService.getAll();
        model.addAttribute("donations", list);
        return "admin/donations";
    }

    @GetMapping("/users")
    public String allUsers(Model model) {
        List<UserDto> users = userService.getAll();
        model.addAttribute("users", users);
        return "admin/users";
    }
}