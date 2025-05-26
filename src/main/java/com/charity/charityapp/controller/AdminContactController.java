package com.charity.charityapp.controller;

import com.charity.charityapp.dto.ContactRequestDto;
import com.charity.charityapp.service.ContactRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/contact/requests")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminContactController {

    private final ContactRequestService contactRequestService;


    @ModelAttribute("requestUri")
    public String requestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }


    private String view = "admin/contact-requests";

    @GetMapping
    public String listAll(Model model, @RequestParam(required = false) Boolean approved,
                          @RequestParam(required = false) Boolean rejected) {
        List<ContactRequestDto> all = contactRequestService.getAll();
        log.info("Admin listAll(): found {} total contact requests", all.size());
        model.addAttribute("requests", all);
        if (Boolean.TRUE.equals(approved))  model.addAttribute("approved", true);
        if (Boolean.TRUE.equals(rejected))  model.addAttribute("rejected", true);
        return view;
    }

    @GetMapping("/pending")
    public String listPending(Model model,
                              @RequestParam(required = false) Boolean approved,
                              @RequestParam(required = false) Boolean rejected) {
        List<ContactRequestDto> pending = contactRequestService.getPending();
        log.info("Admin listPending(): found {} pending contact requests", pending.size());
        model.addAttribute("requests", pending);
        if (Boolean.TRUE.equals(approved))  model.addAttribute("approved", true);
        if (Boolean.TRUE.equals(rejected))  model.addAttribute("rejected", true);
        return view;
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id,
                          @RequestParam(required = false) String adminResponse) {
        log.info("Admin approve(): id={}, response='{}'", id, adminResponse);
        contactRequestService.approve(id, adminResponse);
        return "redirect:/admin/contact/requests?approved=true";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id,
                         @RequestParam(required = false) String adminResponse) {
        log.info("Admin reject(): id={}, response='{}'", id, adminResponse);
        contactRequestService.reject(id, adminResponse);
        return "redirect:/admin/contact/requests?rejected=true";
    }
    @GetMapping("/dump-json")
    @ResponseBody
    public List<ContactRequestDto> dump() {
        return contactRequestService.getAll();
    }

}
