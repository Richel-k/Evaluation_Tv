package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.model.Question;
import com.Application.EvaluationTV.model.ReponseCandidat;
import com.Application.EvaluationTV.model.Session;
import com.Application.EvaluationTV.repository.CandidatRepository;
import com.Application.EvaluationTV.repository.SessionRepository;
import com.Application.EvaluationTV.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final CandidatRepository candidatRepository;

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

    @GetMapping("/end-collaboration")
    public ResponseEntity<Map<String, String>> getMethodName(@RequestParam String param) {

        Map<String, String> obj = new HashMap<>();
        
        
        obj.put("value", "okay");
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/get-My-result")
    public ResponseEntity<?> getMyResult(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<Map<String, Object>> formattedResults = new ArrayList<>();

        List<Object[]> obj = candidatRepository.giveMyResult(userDetails.getId());

        for(Object[] row : obj){

            Map<String, Object> objs = new HashMap<>();
            objs.put("session_id", row[0]);
            objs.put("title", row[1]);
            objs.put("points", row[3]);
            objs.put("date_debut", row[2]);
            Integer somme = sessionRepository.GetAllPoints((String) row[0]);
            objs.put("total", somme);

            formattedResults.add(objs);
        }
        



        return ResponseEntity.ok(formattedResults);
    }
    


    
    
}
