package com.Application.EvaluationTV.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.Application.EvaluationTV.model.*;
//import com.evaluation.model.Examinateur;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalTime heureDebut;

    private LocalDate dateFin;

    private LocalTime heureFin;

    private Integer dureeMinutes; // Durée de l'évaluation

    @Enumerated(EnumType.STRING)
    private Enum.StatutSession statut = Enum.StatutSession.PLANIFIEE;

    @ManyToOne
    @JoinColumn(name = "examinateur_id", nullable = false)
    private Examinateur examinateur;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @ManyToMany(mappedBy = "sessions")
    private List<Candidat> candidats = new ArrayList<>();

    // Méthodes utilitaires
    public void ajouterQuestion(Question question) {
        questions.add(question);
        question.setSession(this);
    }

    public void retirerQuestion(Question question) {
        questions.remove(question);
        question.setSession(null);
    }
}