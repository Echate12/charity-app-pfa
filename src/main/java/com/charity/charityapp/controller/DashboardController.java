package com.charity.charityapp.controller;

import com.charity.charityapp.dto.DonationDto;
import com.charity.charityapp.service.DonationService;
import com.charity.charityapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final DonationService donationService;
    private final UserService userService;

    public DashboardController(DonationService donationService, UserService userService) {
        this.donationService = donationService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        // Get current user's email
        String userEmail = authentication.getName();

        // Get user's donations (you'll need to implement this method in your service)
        // For now, let's just get all donations and filter by user
        List<DonationDto> userDonations = donationService.getAll().stream()
                .filter(d -> d.getDonorEmail() != null && d.getDonorEmail().equals(userEmail))
                .collect(Collectors.toList());

        model.addAttribute("donations", userDonations);
        model.addAttribute("totalDonations", userDonations.size());
        model.addAttribute("userName", authentication.getName());

        return "dashboard"; // This should point to your dashboard.html template
    }
}