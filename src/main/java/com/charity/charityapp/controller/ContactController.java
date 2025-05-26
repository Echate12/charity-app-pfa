package com.charity.charityapp.controller;

import com.charity.charityapp.dto.ContactRequestDto;
import com.charity.charityapp.model.User;
import com.charity.charityapp.service.ContactRequestService;
import com.charity.charityapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactRequestService contactRequestService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String contactForm(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        if (userDetails == null || session == null || session.isNew()) {
            model.addAttribute("sessionExpired", true);
            return "redirect:/auth/user-login";
        }
        model.addAttribute("request", new ContactRequestDto());
        return "user/contact";
    }

    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    public String submitRequest(
            @ModelAttribute("request") ContactRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes flash,
            HttpSession session
    ) {
        if (userDetails == null || session == null || session.isNew()) {
            flash.addFlashAttribute("sessionExpired", true);
            return "redirect:/auth/user-login";
        }

        try {
            // basic validation
            if (dto.getType() == null
                    || dto.getTitle() == null || dto.getTitle().isBlank()
                    || dto.getDescription() == null || dto.getDescription().isBlank()
                    || dto.getContactEmail() == null || dto.getContactEmail().isBlank()
                    || dto.getContactPhone() == null || dto.getContactPhone().isBlank()
            ) {
                flash.addFlashAttribute("error", "Tous les champs obligatoires doivent être remplis.");
                flash.addFlashAttribute("request", dto); // Preserve form data
                return "redirect:/contact";
            }

            User user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            contactRequestService.create(dto, user.getId());
            log.info("Contact request submitted by {}", user.getEmail());

            flash.addFlashAttribute("success", "Votre demande a été soumise à l'administrateur.");
            return "redirect:/contact/my-requests";
            
        } catch (Exception e) {
            log.error("Error submitting contact request: {}", e.getMessage(), e);
            flash.addFlashAttribute("error", "Une erreur est survenue lors de la soumission de votre demande. Veuillez réessayer.");
            flash.addFlashAttribute("request", dto); // Preserve form data
            return "redirect:/contact";
        }
    }

    @GetMapping("/my-requests")
    @PreAuthorize("isAuthenticated()")
    public String myRequests(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            HttpSession session
    ) {
        if (userDetails == null || session == null || session.isNew()) {
            model.addAttribute("sessionExpired", true);
            return "redirect:/auth/user-login";
        }

        try {
            User user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            List<ContactRequestDto> requests = contactRequestService.getByUserId(user.getId());
            model.addAttribute("requests", requests);
            return "user/my-requests";
            
        } catch (Exception e) {
            log.error("Error fetching user requests: {}", e.getMessage(), e);
            model.addAttribute("error", "Une erreur est survenue lors de la récupération de vos demandes. Veuillez réessayer.");
            return "user/my-requests";
        }
    }
}
