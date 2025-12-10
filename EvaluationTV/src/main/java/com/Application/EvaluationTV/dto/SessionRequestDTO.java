// SessionRequestDTO.java
package com.Application.EvaluationTV.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SessionRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 200, message = "Le titre doit contenir entre 3 et 200 caractères")
    private String titre;

    @Size(max = 1000, message = "La description ne peut excéder 1000 caractères")
    private String description;

    @NotNull(message = "La date est obligatoire")
    @FutureOrPresent(message = "La date doit être aujourd'hui ou dans le futur")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateSession;

    @NotNull(message = "L'heure de début est obligatoire")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime heureDebut;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée minimale est 1 minute")
    @Max(value = 300, message = "La durée maximale est 300 minutes")
    private Integer dureeMinutes;

    // @Min(value = 1, message = "Le nombre minimum de candidats est 1")
    // @Max(value = 500, message = "Le nombre maximum de candidats est 500")
    // private Integer nombreMaxCandidats;

    @Min(value = 0, message = "Le score de réussite doit être entre 0 et 100")
    @Max(value = 100, message = "Le score de réussite doit être entre 0 et 100")
    private Integer scoreReussite = 60;

    @NotBlank(message = "L'ordre des questions est obligatoire")
    private String ordreQuestions; // "random", "sequential", "difficulty"

    @NotBlank(message = "La visibilité des résultats est obligatoire")
    private String visibiliteResultats; // "immediate", "after-session", "manual"

    private boolean retourArriereAutorise = true;
    private boolean melangerReponses = false;
    private boolean tempsParQuestion = false;

    @Valid
    @NotEmpty(message = "La session doit contenir au moins une question")
    private List<QuestionRequestDTO> questions;
}