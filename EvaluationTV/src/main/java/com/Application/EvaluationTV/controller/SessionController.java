package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.dto.*;
import com.Application.EvaluationTV.model.Question;
import com.Application.EvaluationTV.model.Session;
import com.Application.EvaluationTV.repository.SessionRepository;
//import com.application.evaluationtv.dto.SessionResponseDTO;
import com.Application.EvaluationTV.service.SessionService;

import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // À configurer selon votre environnement
public class SessionController {

    private final SessionService sessionService;
    private final SessionRepository sessionRepository;
    @PostMapping
    public ResponseEntity<SessionResponseDTO> createSession(
            @Valid @RequestBody SessionRequestDTO sessionRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Récupérer l'ID de l'examinateur connecté
        String username = userDetails.getUsername();

        // Appeler le service pour créer la session
        SessionResponseDTO createdSession = sessionService.createSession(sessionRequest, username);
        System.out.println("Nous sommes ici");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    // Gestion des erreurs
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    @GetMapping("/{sessionId}/questions")
    public List<Question> getQuestionForSession(@PathVariable String sessionId, Model model) {

        Optional<Session> sessionOptional = sessionRepository.findByCode(sessionId);

        if(sessionOptional.isPresent()){
            Session session = sessionOptional.get();
            List<Question> questions = sessionRepository.AllQuestionOfMe(sessionId);//new ArrayList<>();
            //List<Optional<Question>> optQuestion = sessionRepository.AllQuestionOfMe(sessionId);

            return questions;
        }
        return new ArrayList<>();
    }
    

    
}