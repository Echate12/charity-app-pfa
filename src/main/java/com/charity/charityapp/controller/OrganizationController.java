package com.charity.charityapp.controller;

import com.charity.charityapp.dto.OrganizationDto;
import com.charity.charityapp.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService service;

    @GetMapping
    public String list(Model model) {
        List<OrganizationDto> orgs = service.getAll();
        model.addAttribute("organizations", orgs);
        return "organizations/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("organization", new OrganizationDto());
        return "organizations/form";
    }

    @PostMapping
    public String create(@ModelAttribute OrganizationDto dto) {
        service.create(dto);
        return "redirect:/organizations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("organization", service.getById(id));
        return "organizations/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute OrganizationDto dto) {
        service.update(id, dto);
        return "redirect:/organizations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/organizations";
    }
}