package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/examinateur")
@RequiredArgsConstructor
public class ExaminateurController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('EXAMINATEUR')")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("examinateur", userDetails.getPerson());
        model.addAttribute("nomComplet", userDetails.getNomComplet());
        model.addAttribute("MyEmail",  userDetails.getMyEmail());
        return "examinateur/dashboard";
    }

    @GetMapping("/create-session")
    public String CreateSession() {
        return "examinateur/creer-session";
    }

    @GetMapping("/update-session")
    public String UpdateSession() {
        return "examinateur/modifier-session";
    }
    
}