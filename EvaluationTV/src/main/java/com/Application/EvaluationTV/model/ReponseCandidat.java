package com.Application.EvaluationTV.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reponses_candidat")
public class ReponseCandidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(length = 5000)
    private String reponseTexte; // Pour les questions ouvertes

    @ManyToOne
    @JoinColumn(name = "option_choisie_id")
    private OptionReponse optionChoisie; // Pour les QCM

    private LocalDateTime dateReponse;

    private Integer pointsObtenus;

    @Column(length = 1000)
    private String commentaireExaminateur;

    @Enumerated(EnumType.STRING)
    private Enum.StatutReponse statut = Enum.StatutReponse.EN_ATTENTE;
}