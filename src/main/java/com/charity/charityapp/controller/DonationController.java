package com.charity.charityapp.controller;

import com.charity.charityapp.dto.DonationDto;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.DonationService;
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
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService service;
    private final CharityActionService charityActionService;

    @GetMapping
    public String list(Model model) {
        List<DonationDto> donations = service.getAll();
        model.addAttribute("donations", donations);
        return "donations/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("donation", new DonationDto());
        model.addAttribute("charityActions", charityActionService.getAll());
        return "donations/create";
    }

    @PostMapping
    public String create(@ModelAttribute DonationDto dto) {
        service.create(dto);
        return "redirect:/donations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("donation", service.getById(id));
        model.addAttribute("charityActions", charityActionService.getAll());
        return "donations/update";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute DonationDto dto) {
        service.update(id, dto);
        return "redirect:/donations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/donations";
    }
}