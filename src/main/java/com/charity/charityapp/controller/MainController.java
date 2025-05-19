package com.charity.charityapp.controller;

import com.charity.charityapp.model.CharityAction;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private final CharityActionService charityActionService;


    @GetMapping("/")
    public String home(Model model) {
        List<CharityAction> popularActions = charityActionService.getAll();
        model.addAttribute("actions", popularActions);
        return "index";
    }
}
