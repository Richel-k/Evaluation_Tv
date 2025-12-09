package com.Application.EvaluationTV.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String enonce;

    @Enumerated(EnumType.STRING)
    private Enum.TypeQuestion type; // QCM, VRAI_FAUX, TEXTE_LIBRE, etc.

    private Integer points; // Points attribués à cette question

    private Integer ordre; // Ordre d'affichage dans la session

    @Column(length = 2000)
    private String explication; // Explication de la réponse correcte

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionReponse> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReponseCandidat> reponsesCandidats = new ArrayList<>();
}