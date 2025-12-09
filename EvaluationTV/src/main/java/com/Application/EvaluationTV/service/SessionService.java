package com.Application.EvaluationTV.service;

import com.Application.EvaluationTV.dto.*;
//import com.application.evaluationtv.dto.SessionResponseDTO;
import com.Application.EvaluationTV.model.*;
import com.Application.EvaluationTV.model.Enum.StatutSession;
import com.Application.EvaluationTV.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final ReponseRepository reponseRepository;
    private final ExaminateurRepository examinateurRepository;

    public SessionResponseDTO createSession(SessionRequestDTO sessionRequest, String usernameExaminateur) {
        // 1. Vérifier que l'examinateur existe
        Examinateur examinateur = examinateurRepository.findByEmail(usernameExaminateur)
                .orElseThrow(() -> new IllegalArgumentException("Examinateur non trouvé"));

        // 2. Créer la session
        Session session = new Session();
        session.setTitre(sessionRequest.getTitre());
        session.setDescription(sessionRequest.getDescription());
        session.setDateSession(sessionRequest.getDateSession().atTime(sessionRequest.getHeureDebut()));
        session.setDureeMinutes(sessionRequest.getDureeMinutes());
        session.setNombreMaxCandidats(sessionRequest.getNombreMaxCandidats());
        session.setScoreReussite(sessionRequest.getScoreReussite());
        //session.setOrdreQuestions(sessionRequest.getOrdreQuestions());
        //session.setVisibiliteResultats(sessionRequest.getVisibiliteResultats());
        //session.setRetourArriereAutorise(sessionRequest.isRetourArriereAutorise());
        //session.setMelangerReponses(sessionRequest.isMelangerReponses());
        //session.setTempsParQuestion(sessionRequest.isTempsParQuestion());
        session.setExaminateur(examinateur);
        session.setCode(generateSessionCode());
        session.setStatut(StatutSession.PLANIFIEE);
        session.setDateDebut(LocalDateTime.now());

        // Sauvegarder la session
        Session savedSession = sessionRepository.save(session);

        // 3. Créer les questions et réponses
        List<Question> questions = new ArrayList<>();

        for (QuestionRequestDTO questionDto : sessionRequest.getQuestions()) {
            Question question = new Question();
            question.setEnonce(questionDto.getEnonce());
            question.setTypeQuestion(questionDto.getTypeQuestion());
            question.setPoints(questionDto.getPoints());
            question.setDifficulte(questionDto.getDifficulte());
            question.setNumero(questionDto.getNumero());
            question.setSession(savedSession);

            Question savedQuestion = questionRepository.save(question);

            // Créer les réponses si nécessaire
            if (questionDto.getReponses() != null && !questionDto.getReponses().isEmpty()) {
                List<Reponse> reponses = new ArrayList<>();

                for (ReponseRequestDTO reponseDto : questionDto.getReponses()) {
                    Reponse reponse = new Reponse();
                    reponse.setTexte(reponseDto.getTexte());
                    reponse.setCorrecte(reponseDto.getCorrecte());
                    reponse.setOrdre(reponseDto.getOrdre());
                    reponse.setQuestion(savedQuestion);

                    reponses.add(reponse);
                }

                reponseRepository.saveAll(reponses);
                savedQuestion.setReponses(reponses);
            }

            questions.add(savedQuestion);
        }

        savedSession.setQuestions(questions);

        // 4. Retourner la réponse DTO
        return convertToResponseDTO(savedSession);
    }

    private String generateSessionCode() {
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "SESS-" + uuid;
    }

    private SessionResponseDTO convertToResponseDTO(Session session) {
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setId(session.getId());
        dto.setTitre(session.getTitre());
        dto.setCodeSession(session.getCode());
        dto.setDateDebut(session.getDateDebut());
        dto.setDureeMinutes(session.getDureeMinutes());
        dto.setStatut(session.getStatut());
        dto.setNombreQuestions(session.getQuestions() != null ? session.getQuestions().size() : 0);
        dto.setDateCreation(session.getDateCreation());
        dto.setExaminateurNom(session.getExaminateur().getNomComplet());
        dto.setExaminateurEmail(session.getExaminateur().getEmail());

        return dto;
    }
}