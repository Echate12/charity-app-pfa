package com.charity.charityapp.controller;

import com.charity.charityapp.dto.DonationDto;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.DonationService;
import com.charity.charityapp.service.OrganizationService;
import com.charity.charityapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final CharityActionService actionService;
    private final DonationService donationService;
    private final OrganizationService organizationService;
    private final UserService userService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        int totalActions       = actionService.getAll().size();
        long totalDonations    = donationService.countAll();
        int totalOrganizations = organizationService.getAll().size();
        int totalUsers         = userService.getAll().size();

        List<DonationDto> recentDonations = donationService.getAll().stream()
                .sorted((d1, d2) -> d2.getDonatedAt().compareTo(d1.getDonatedAt()))
                .limit(5)
                .toList();

        model.addAttribute("totalActions", totalActions);
        model.addAttribute("totalDonations", totalDonations);
        model.addAttribute("totalOrganizations", totalOrganizations);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("recentDonations", recentDonations);

        return "dashboard";
    }
}