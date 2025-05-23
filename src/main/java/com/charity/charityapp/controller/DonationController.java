package com.charity.charityapp.controller;

import com.charity.charityapp.dto.DonationDto;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;
    private final CharityActionService charityActionService;

    // 1) LIST all donations
    @GetMapping
    public String list(Model model) {
        model.addAttribute("donations", donationService.getAll());
        return "donations/index";
    }

    // 2) SHOW the "create" form
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("donation", new DonationDto());
        model.addAttribute("charityActions", charityActionService.getAll());
        return "donations/create";
    }

    // 3) HANDLE the "create" POST
    @PostMapping("/create")
    public String create(@ModelAttribute("donation") DonationDto dto) {
        donationService.create(dto);
        return "redirect:/donations";
    }

    // 4) SHOW the "edit" form
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        DonationDto existing = donationService.getById(id);
        model.addAttribute("donation", existing);
        model.addAttribute("charityActions", charityActionService.getAll());
        return "donations/edit";
    }

    // 5) HANDLE the "edit" POST
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("donation") DonationDto dto) {
        donationService.update(id, dto);
        return "redirect:/donations";
    }

    // 6) DELETE (via GET for simplicity)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        donationService.delete(id);
        return "redirect:/donations";
    }
}
