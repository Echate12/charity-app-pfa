package com.charity.charityapp.controller;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/actions")
@RequiredArgsConstructor
public class CharityActionController {

    private final CharityActionService actionService;
    private final OrganizationService organizationService;

    /** List all actions, with optional search */
    @GetMapping
    public String list(@RequestParam(name = "q", required = false) String q,
                       Model model) {
        List<CharityActionDto> actions = (q != null && !q.isBlank())
                ? actionService.search(q)
                : actionService.getAll();
        model.addAttribute("actions", actions);
        model.addAttribute("q", q);
        return "charityActions/index";
    }

    /** Show the create form (ADMIN only) */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("action", new CharityActionDto());
        model.addAttribute("organizations", organizationService.getAll());
        return "charityActions/create";
    }

    /** Handle create submission (ADMIN only) */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String create(@ModelAttribute("action") CharityActionDto dto) {
        actionService.create(dto);
        return "redirect:/actions";
    }

    /** Show the edit form (ADMIN only) */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CharityActionDto dto = actionService.getById(id);
        model.addAttribute("action", dto);
        model.addAttribute("organizations", organizationService.getAll());
        return "charityActions/edit";
    }

    /** Handle update submission (ADMIN only) */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("action") CharityActionDto dto) {
        actionService.update(id, dto);
        return "redirect:/actions";
    }

    /** Handle delete (ADMIN only) via POST to support CSRF */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        actionService.delete(id);
        return "redirect:/actions";
    }

    /** Follow or unfollow an action (any authenticated user) */
    @PostMapping("/follow/{id}")
    public String follow(@PathVariable Long id, Principal principal) {
        actionService.follow(id, principal.getName());
        return "redirect:/actions";
    }

    /** Show tracked actions for the current user */
    @GetMapping("/tracked")
    public String tracked(Principal principal, Model model) {
        model.addAttribute("actions", actionService.getTracked(principal.getName()));
        return "tracker/tracked";
    }
}
