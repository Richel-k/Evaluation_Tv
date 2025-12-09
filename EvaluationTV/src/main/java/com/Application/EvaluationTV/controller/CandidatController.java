package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidat")
@RequiredArgsConstructor
public class CandidatController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('CANDIDAT')")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("candidat", userDetails.getPerson());
        model.addAttribute("nomComplet", userDetails.getNomComplet());
        return "candidat/dashboard";
    }
}
