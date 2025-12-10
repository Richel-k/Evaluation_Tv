package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.model.Session;
import com.Application.EvaluationTV.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Application.EvaluationTV.repository.SessionRepository;


@Controller
@RequestMapping("/examinateur")
@RequiredArgsConstructor
public class ExaminateurController {
    private final SessionRepository sessionRepository;


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