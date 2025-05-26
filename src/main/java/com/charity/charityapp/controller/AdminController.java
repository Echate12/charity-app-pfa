package com.charity.charityapp.controller;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.dto.OrganizationDto;
import com.charity.charityapp.dto.UserDto;
import com.charity.charityapp.enums.OrganizationStatus;
import com.charity.charityapp.enums.Role;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.ContactRequestService;
import com.charity.charityapp.service.DonationService;
import com.charity.charityapp.service.OrganizationService;
import com.charity.charityapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminController handles all admin-related views and actions:
 * - Dashboard overview
 * - User listing
 * - Donation listing
 * - CharityAction CRUD
 * - Organization CRUD
 * - ContactRequest management
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final DonationService donationService;
    private final UserService userService;
    private final CharityActionService actionService;
    private final OrganizationService organizationService;
    private final ContactRequestService contactRequestService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalDonations", donationService.countAll());
        model.addAttribute("totalUsers", userService.count());
        model.addAttribute("totalOrganizations", organizationService.count());
        model.addAttribute("pendingOrganizations", organizationService.countByStatus(OrganizationStatus.PENDING.name()));
        model.addAttribute("pendingRequestsCount", contactRequestService.countByStatus("PENDING"));
        model.addAttribute("totalRequestsCount", contactRequestService.count());
        return "admin/dashboard";
    }


    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("roles", Role.values());
        return "admin/users";
    }

    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("roles", Role.values());
        return "admin/users/create";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute UserDto userDto) {
        userService.create(userDto);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("roles", Role.values());
        return "admin/users/edit";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDto userDto) {
        userService.update(id, userDto);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }


    @GetMapping("/donations")
    public String listDonations(Model model, Pageable pageable) {
        model.addAttribute("donations", donationService.getAll(pageable));
        return "admin/donations";
    }

    @GetMapping("/donations/{id}")
    public String viewDonation(@PathVariable Long id, Model model) {
        model.addAttribute("donation", donationService.getById(id));
        return "admin/donation-details";
    }

    @PostMapping("/donations/delete/{id}")
    public String deleteDonation(@PathVariable Long id) {
        donationService.delete(id);
        return "redirect:/admin/donations?deleted=true";
    }


    @GetMapping({"/charity-actions", "/charity-actions/charity-actions.html"})
    public String listActions(Model model, Pageable pageable) {
        model.addAttribute("actions", actionService.getAll(pageable));
        return "admin/charity-actions";
    }

    @GetMapping({"/charity-actions/create", "/charity-actions/create.html"})
    public String showCreateActionForm(Model model) {
        model.addAttribute("action", new CharityActionDto());
        model.addAttribute("categories", actionService.getAllCategories());
        model.addAttribute("organizations", organizationService.getAll());
        return "admin/charity-actions/create";
    }

    @PostMapping({"/charity-actions/create", "/charity-actions/create.html"})
    public String createAction(@ModelAttribute CharityActionDto actionDto) {
        actionService.create(actionDto);
        return "redirect:/admin/charity-actions";
    }

    @GetMapping({"/charity-actions/edit/{id}", "/charity-actions/edit/{id}.html"})
    public String showEditActionForm(@PathVariable Long id, Model model) {
        CharityActionDto dto = actionService.getById(id);
        model.addAttribute("action", dto);
        model.addAttribute("categories", actionService.getAllCategories());
        model.addAttribute("organizations", organizationService.getAll());
        return "admin/charity-actions/edit";
    }

    @PostMapping({"/charity-actions/edit/{id}", "/charity-actions/edit/{id}.html"})
    public String updateAction(@PathVariable Long id,
                               @ModelAttribute CharityActionDto actionDto) {
        actionService.update(id, actionDto);
        return "redirect:/admin/charity-actions";
    }

    @GetMapping({"/charity-actions/delete/{id}", "/charity-actions/delete/{id}.html"})
    public String deleteAction(@PathVariable Long id) {
        actionService.delete(id);
        return "redirect:/admin/charity-actions";
    }


    @GetMapping("/organizations")
    public String listOrganizations(Model model) {
        model.addAttribute("organizations", organizationService.getAll());
        model.addAttribute("statuses", OrganizationStatus.values());
        return "admin/organizations";
    }

    @GetMapping("/organizations/create")
    public String showCreateOrganizationForm(Model model) {
        model.addAttribute("organization", new OrganizationDto());
        return "admin/organizations/create";
    }

    @PostMapping("/organizations/create")
    public String createOrganization(@ModelAttribute OrganizationDto dto) {
        organizationService.create(dto);
        return "redirect:/admin/organizations";
    }

    @GetMapping("/organizations/edit/{id}")
    public String showEditOrganizationForm(@PathVariable Long id, Model model) {
        model.addAttribute("organization", organizationService.getById(id));
        model.addAttribute("statuses", OrganizationStatus.values());
        return "admin/organizations/edit";
    }

    @PostMapping("/organizations/edit/{id}")
    public String updateOrganization(@PathVariable Long id, @ModelAttribute OrganizationDto dto) {
        organizationService.update(id, dto);
        return "redirect:/admin/organizations";
    }

    @PostMapping("/organizations/delete/{id}")
    public String deleteOrganization(@PathVariable Long id) {
        organizationService.delete(id);
        return "redirect:/admin/organizations";
    }


}
