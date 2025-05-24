package com.charity.charityapp.controller;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.dto.DonationDto;
import com.charity.charityapp.dto.OrganizationDto;
import com.charity.charityapp.dto.UserDto;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.DonationService;
import com.charity.charityapp.service.OrganizationService;
import com.charity.charityapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final DonationService donationService;
    private final UserService userService;
    private final CharityActionService actionService;
    private final OrganizationService organizationService;

    // ----- Dashboard -----
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalDonations", donationService.countAll());
        model.addAttribute("totalUsers", userService.getAll().size());
        return "admin/dashboard";
    }

    // ----- Users -----
    @GetMapping("/users")
    public String allUsers(Model model) {
        List<UserDto> users = userService.getAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // ----- Donations -----
    @GetMapping("/donations")
    public String allDonations(Model model) {
        List<DonationDto> donations = donationService.getAll();
        model.addAttribute("donations", donations);
        return "admin/donations";
    }

    // ----- Charity Actions -----
    @GetMapping("/charity-actions")
    public String allCharityActions(Model model) {
        List<CharityActionDto> actions = actionService.getAll();
        model.addAttribute("actions", actions);
        return "admin/charity-actions";
    }

    // ----- Organizations CRUD -----

    /** List all organizations (admin view) */
    @GetMapping("/organizations")
    public String allOrganizations(Model model) {
        List<OrganizationDto> orgs = organizationService.getAll();
        model.addAttribute("organizations", orgs);
        return "admin/organizations";  // src/main/resources/templates/admin/organizations.html
    }

    /** Show create form */
    @GetMapping("/organizations/create")
    public String showCreateOrganizationForm(Model model) {
        model.addAttribute("organization", new OrganizationDto());
        return "admin/organizations/create";
    }

    /** Handle create submission */
    @PostMapping("/organizations/create")
    public String createOrganization(@ModelAttribute OrganizationDto dto) {
        organizationService.create(dto);
        return "redirect:/admin/organizations";
    }

    /** Show edit form */
    @GetMapping("/organizations/edit/{id}")
    public String showEditOrganizationForm(@PathVariable Long id, Model model) {
        model.addAttribute("organization", organizationService.getById(id));
        return "admin/organizations/edit";
    }

    /** Handle update submission */
    @PostMapping("/organizations/edit/{id}")
    public String updateOrganization(@PathVariable Long id,
                                     @ModelAttribute OrganizationDto dto) {
        organizationService.update(id, dto);
        return "redirect:/admin/organizations";
    }

    /** Delete an organization */
    @GetMapping("/organizations/delete/{id}")
    public String deleteOrganization(@PathVariable Long id) {
        organizationService.delete(id);
        return "redirect:/admin/organizations";
    }
}
