package com.charity.charityapp.controller;

import com.charity.charityapp.dto.OrganizationDto;
import com.charity.charityapp.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService service;

    /**
     * Anyone can view the list of organizations.
     */
    @GetMapping
    public String list(Model model) {
        List<OrganizationDto> orgs = service.getAll();
        model.addAttribute("organizations", orgs);
        return "organizations/list";  // src/main/resources/templates/organizations/list.html
    }
}
