package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Application.EvaluationTV.service.AuthService;

import jakarta.validation.Valid;

import com.Application.EvaluationTV.dto.*;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Page de choix du type d'inscription
    @GetMapping("/")
    public String registerChoice() {
        return "auth/register-choice";
    }

    

    // ========== CANDIDAT ==========

    @GetMapping("/candidat")
    public String showCandidatRegistrationForm(Model model) {
        model.addAttribute("candidat", new CandidatRegistrationDto());
        return "auth/register-candidat";
    }

    @PostMapping("/candidat")
    public String registerCandidat(
            @Valid @ModelAttribute("candidat") CandidatRegistrationDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Validation des erreurs
        if (result.hasErrors()) {
            return "auth/register-candidat";
        }

        // Vérifier que les mots de passe correspondent
        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            result.rejectValue("confirmMotDePasse", "error.candidat",
                    "Les mots de passe ne correspondent pas");
            return "auth/register-candidat";
        }

        try {
            authService.registerCandidat(dto);
            redirectAttributes.addFlashAttribute("success",
                    "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.candidat", e.getMessage());
            return "auth/register-candidat";
        }
    }

    // ========== EXAMINATEUR ==========

    @GetMapping("/examinateur")
    public String showExaminateurRegistrationForm(Model model) {
        model.addAttribute("examinateur", new ExaminateurRegistrationDto());
        return "auth/register-examinateur"; // KEMBOU FOSSO RICHEL - richel.kembou@facsciences-uy1.cm - 1234567
    }

    @PostMapping("/examinateur")
    public String registerExaminateur(
            @Valid @ModelAttribute("examinateur") ExaminateurRegistrationDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Validation des erreurs
        if (result.hasErrors()) {
            return "auth/register-examinateur";
        }

        // Vérifier que les mots de passe correspondent
        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            result.rejectValue("confirmMotDePasse", "error.examinateur",
                    "Les mots de passe ne correspondent pas");
            return "auth/register-examinateur";
        }

        try {
            authService.registerExaminateur(dto);
            redirectAttributes.addFlashAttribute("success",
                    "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.examinateur", e.getMessage());
            return "auth/register-examinateur";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getPerson());
        model.addAttribute("nomComplet", userDetails.getNomComplet());
        model.addAttribute("userType", userDetails.getUserType());
        model.addAttribute("MyEmail", userDetails.getMyEmail());

        // Redirection basée sur le rôle
        if (userDetails.isCandidat()) {
            return "redirect:/candidat/dashboard";
        } else if (userDetails.isExaminateur()) {
            return "redirect:/examinateur/dashboard";
        }

        return "home";
    }
}