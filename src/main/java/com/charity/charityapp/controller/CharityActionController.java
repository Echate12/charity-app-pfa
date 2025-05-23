package com.charity.charityapp.controller;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.service.CharityActionService;
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
@RequestMapping("/actions")
@RequiredArgsConstructor
public class CharityActionController {
    private final CharityActionService service;
    private final OrganizationService organizationService;

    @GetMapping
    public String list(Model model) {
        List<CharityActionDto> actions = service.getAll();
        model.addAttribute("actions", actions);
        return "charityActions/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("action", new CharityActionDto());
        model.addAttribute("organizations", organizationService.getAll());
        return "charityActions/create";
    }

    @PostMapping
    public String create(@ModelAttribute CharityActionDto dto) {
        service.create(dto);
        return "redirect:/actions";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("action", service.getById(id));
        model.addAttribute("organizations", organizationService.getAll());
        return "charityActions/update";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute CharityActionDto dto) {
        service.update(id, dto);
        return "redirect:/actions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/actions";
    }
}
