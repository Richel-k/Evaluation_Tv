package com.Application.EvaluationTV.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "options_reponse")
public class OptionReponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String texte;

    @Column(nullable = false)
    private Boolean estCorrecte = false;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
