package com.charity.charityapp.controller;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.dto.OrganizationDto;
import com.charity.charityapp.enums.OrganizationStatus;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserDashboardController {

    private final CharityActionService actionService;
    private final OrganizationService organizationService;

    @GetMapping
    public String dashboard(Model model) {
        // Get approved organizations only
        List<OrganizationDto> organizations = organizationService.getAll().stream()
                .filter(org -> org.getStatus() == OrganizationStatus.APPROVED)
                .toList();

        // Get all charity actions for user
        List<CharityActionDto> actions = actionService.getAllForUser();

        model.addAttribute("organizations", organizations);
        model.addAttribute("actions", actions);
        return "user/dashboard";
    }

    @GetMapping("/charity-actions")
    public String listActions(Model model) {
        List<CharityActionDto> actions = actionService.getAllForUser();
        model.addAttribute("actions", actions);
        return "user/charity-actions";
    }

    @GetMapping("/organizations")
    public String listOrganizations(Model model) {
        List<OrganizationDto> organizations = organizationService.getAll().stream()
                .filter(org -> org.getStatus() == OrganizationStatus.APPROVED)
                .toList();
        model.addAttribute("organizations", organizations);
        return "user/organizations";
    }
}
