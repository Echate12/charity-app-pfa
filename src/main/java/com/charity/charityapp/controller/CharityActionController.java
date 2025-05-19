package com.charity.charityapp.controller;

import com.charity.charityapp.dto.CharityActionDto;
import com.charity.charityapp.enums.ActionCategory;
import com.charity.charityapp.exceptions.ResourceNotFoundException;
import com.charity.charityapp.model.CharityAction;
import com.charity.charityapp.model.Organization;
import com.charity.charityapp.service.CharityActionService;
import com.charity.charityapp.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/charityActions")
@AllArgsConstructor
public class CharityActionController {

    private final CharityActionService charityActionService;
    private final OrganizationService organizationService;

    @GetMapping
    public String listCharityActions(Model model,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(required = false) String keyword) {
        List<CharityAction> actions;

        if (keyword != null && !keyword.isEmpty()) {
            actions = charityActionService.searchActions(keyword, ActionCategory.valueOf(category));
        } else if (category != null && !category.isEmpty()) {
            actions = charityActionService.findByCategory(ActionCategory.valueOf(category));
        } else {
            actions = charityActionService.findAllActions();
        }

        model.addAttribute("actions", actions);
        model.addAttribute("categories", ActionCategory.values());
        return "charityActions/index";
    }

    // VIEW SINGLE ACTION
    @GetMapping("/{id}")
    public String viewCharityAction(@PathVariable Long id, Model model) {
        CharityAction action = charityActionService.findActionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Action not found"));
        model.addAttribute("action", action);
        return "charityActions/show";
    }

    // SHOW CREATE FORM
    @GetMapping("/new")
    public String showCreateForm(Model model, Principal principal) {
        Organization org = organizationService.findByUserEmail(principal.getName())
                .orElseThrow(() -> new AccessDeniedException("Only organizations can create actions"));

        CharityActionDto actionDto = new CharityActionDto();
        actionDto.setOrganizationId(org.getId());

        model.addAttribute("action", actionDto);
        model.addAttribute("categories", ActionCategory.values());
        return "charityActions/create";
    }

    // CREATE ACTION
    @PostMapping
    public String createCharityAction(@ModelAttribute("action") CharityActionDto actionDto,
                                      BindingResult result,
                                      Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", ActionCategory.values());
            return "charityActions/create";
        }

        CharityAction savedAction = charityActionService.save(actionDto);
        return "redirect:/charityActions/" + savedAction.getId();
    }

    // SHOW EDIT FORM
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        CharityAction action = charityActionService.findActionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Action not found"));

        // Verify organization ownership
        if (!action.getOrganization().getContactEmail().equals(principal.getName())) {
            throw new RuntimeException("You don't have permission to edit this action");
        }

        model.addAttribute("action", action);
        model.addAttribute("categories", ActionCategory.values());
        return "charityActions/edit";
    }

    // UPDATE ACTION
    @PostMapping("/{id}")
    public String updateCharityAction(@PathVariable Long id,
                                      @ModelAttribute("action") CharityActionDto actionDto) {
        charityActionService.update(id, actionDto);
        return "redirect:/charityActions" + id;
    }

    // DELETE ACTION
    @PostMapping("/{id}/delete")
    public String deleteCharityAction(@PathVariable Long id) {
        charityActionService.deleteById(id);
        return "redirect:/charityActions/index";
    }
}
