package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.dto.*;
//import com.application.evaluationtv.dto.SessionResponseDTO;
import com.Application.EvaluationTV.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<SessionResponseDTO> createSession(
            @Valid @RequestBody SessionRequestDTO sessionRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Récupérer l'ID de l'examinateur connecté
        String username = userDetails.getUsername();

        // Appeler le service pour créer la session
        SessionResponseDTO createdSession = sessionService.createSession(sessionRequest, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    // Gestion des erreurs
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}