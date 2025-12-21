package com.Application.EvaluationTV.controller;

import com.Application.EvaluationTV.dto.*;
import com.Application.EvaluationTV.model.Candidat;
import com.Application.EvaluationTV.model.OptionReponse;
import com.Application.EvaluationTV.model.Question;
import com.Application.EvaluationTV.model.ReponseCandidat;
import com.Application.EvaluationTV.model.Session;
import com.Application.EvaluationTV.repository.CandidatRepository;
import com.Application.EvaluationTV.repository.QuestionRepository;
import com.Application.EvaluationTV.repository.ReponseCandidatRepository;
import com.Application.EvaluationTV.repository.ReponseRepository;
import com.Application.EvaluationTV.repository.SessionRepository;
import com.Application.EvaluationTV.security.CustomUserDetails;
//import com.application.evaluationtv.dto.SessionResponseDTO;
import com.Application.EvaluationTV.service.SessionService;

import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // À configurer selon votre environnement
public class SessionController {

    private final SessionService sessionService;
    private final SessionRepository sessionRepository;
    private final ReponseRepository reponseRepository;
    private final QuestionRepository questionRepository;
    private final CandidatRepository candidatRepository;
    private final ReponseCandidatRepository reponseCandidatRepository;

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
    public ResponseEntity<List<QuestionRequestDTO>> getQuestionForSession(@PathVariable String sessionId, Model model) {

        Optional<Session> sessionOptional = sessionRepository.findByCode(sessionId);
        List<Question> questions;
        if(sessionOptional.isPresent()){
            Session session = sessionOptional.get();
/*List<Question> */questions = sessionRepository.AllQuestionOfMe(sessionId);//new ArrayList<>();
            //List<Optional<Question>> optQuestion = sessionRepository.AllQuestionOfMe(sessionId);
            System.out.println(questions.get(0).getEnonce());

            List<QuestionRequestDTO> questionDtos = questions.stream()
            .map(q -> new QuestionRequestDTO(q.getId(), q.getEnonce(), q.getPoints(), q.getTime()))
            .collect(Collectors.toList());

            questionDtos.forEach(q -> {
                List<ReponseRequestDTO> responses = reponseRepository.AllResponseOfMe(q.getNumero());
                q.setReponses(responses);
            });
            
            return ResponseEntity.ok(questionDtos);
            //return ResponseEntity.ok(questions);
        }
        return ResponseEntity.ok(new ArrayList<>());
        //QuestionRequestDTO
    }



    @PostMapping("/evaluation/submitEvaluation")
    public ResponseEntity<Map<String, Object>> postMethodName( @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody Map<String, Object> payload) {
        //TODO: process POST request
        // 1. Extraire les champs simples
    String sessionId = (String) payload.get("sessionId");
    Integer duration = (Integer) payload.get("duration");
    String completionTime = (String) payload.get("completionTime");
    
    System.out.println("Session ID: " + sessionId);
    System.out.println("Durée: " + duration);
    System.out.println("Heure de fin: " + completionTime);

    Long MyId = userDetails.getId();
    //Integer PointsTotal = 0;
    
    // 2. Extraire le tableau des réponses
    List<Map<String, Object>> answers = (List<Map<String, Object>>) payload.get("answers");
    
    // 3. Parcourir toutes les réponses
    for (Map<String, Object> answer : answers) {
        Integer questionIndex = (Integer) answer.get("questionIndex");
        Integer answerIndex = (Integer) answer.get("answerIndex");
        // Gérer que questionId peut être Long ou Integer selon la DB
        Long questionId = Long.parseLong(answer.get("questionId").toString());
        Long reponseId = Long.parseLong(answer.get("answerId").toString());
        Boolean correct = (Boolean) answer.get("correct");
        Integer PointsTotal = 0;
        Integer Points = (Integer) answer.get("points");


        if(correct == true){
            PointsTotal += Points;
        }
        // Ici on peut séléctionner le Candidat et la Question
        Optional<Candidat> candidatOpt = candidatRepository.findById(MyId);
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        Optional<OptionReponse> optionReponse  = reponseRepository.findById(reponseId);

        if(candidatOpt.isPresent() & questionOpt.isPresent() & optionReponse.isPresent()){
            Candidat candidat = candidatOpt.get();
            Question question = questionOpt.get();
            OptionReponse reponse = optionReponse.get();

            ReponseCandidat reponseCandidat = new ReponseCandidat();
            reponseCandidat.setCandidat(candidat);
            reponseCandidat.setQuestion(question);
            reponseCandidat.setOptionChoisie(reponse);

            reponseCandidat.setPointsObtenus(PointsTotal);

            reponseCandidatRepository.save(reponseCandidat);
            
        }


        
        

        //Long questionId = null;
        
        // if (questionIdObj instanceof Integer) {
        //     questionId = ((Integer) questionIdObj).longValue();
        // } else if (questionIdObj instanceof Long) {
        //     questionId = (Long) questionIdObj;
        // } else if (questionIdObj != null) {
        //     questionId = Long.parseLong(questionIdObj.toString());
        // }
        
        System.out.println("Question " + questionIndex + 
                          " -> Réponse " + answerIndex + 
                          " (ID: " + questionId + ")");
        
        // Ici, tu peux traiter chaque réponse
        // Par exemple, sauvegarder en base de données
    }

    
    // 4. Construire la réponse
    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("message", "Évaluation reçue avec succès");
    response.put("sessionId", sessionId);
    response.put("totalAnswers", answers != null ? answers.size() : 0);
    //response.put("processedAt", new Date());
        
        return ResponseEntity.ok(response);
    }


    
    
    

    
}