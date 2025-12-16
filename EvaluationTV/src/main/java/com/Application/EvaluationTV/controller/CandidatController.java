package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.model.Question;
import com.Application.EvaluationTV.model.Session;
import com.Application.EvaluationTV.repository.SessionRepository;
import com.Application.EvaluationTV.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/candidat")
@RequiredArgsConstructor
public class CandidatController {

    private final SessionRepository sessionRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('CANDIDAT')")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("candidat", userDetails.getPerson());
        model.addAttribute("nomComplet", userDetails.getNomComplet());
        return "candidat/dashboard";
    }


    @GetMapping("/session/evaluation/{codeSession}")
    public String editSession(@PathVariable String codeSession, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Optional<Session> session = sessionRepository.findByCode(codeSession);

        if(!session.isPresent()){
            
            return "/candidat/dashboard";
        }
        
        Session sessionad = session.get();
        model.addAttribute("codeSession", sessionad.getCode());
        model.addAttribute("question", sessionad.getQuestions());
        model.addAttribute("email", userDetails.getNomComplet());
        model.addAttribute("codeSession", codeSession);
        return "candidat/evaluation";
    }

    // @GetMapping("/{CodeSession}/questions")
    // public ResponseEntity<List<Question>> getQuestionsOfSession(@PathVariable String CodeSession) {

    //     Optional<Session> session = sessionRepository.findByCode(CodeSession);
    //     if(session.isPresent()){
    //         return ResponseEntity.ok(session.get().getQuestions());
    //     }
        
    //     //return ;
    // }
    
    
}
