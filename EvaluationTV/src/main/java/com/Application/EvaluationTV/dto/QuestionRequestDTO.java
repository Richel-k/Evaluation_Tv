/*QuestionRequestDTO.java*/

package com.Application.EvaluationTV.dto;  

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class QuestionRequestDTO {

    @NotNull(message = "Le numéro de la question est obligatoire")
    @Min(value = 1, message = "Le numéro de la question doit être positif")
    private Integer numero;

    @NotBlank(message = "L'énoncé de la question est obligatoire")
    @Size(min = 5, max = 2000, message = "L'énoncé doit contenir entre 5 et 2000 caractères")
    private String enonce;

    // @NotBlank(message = "Le type de question est obligatoire")
    // private String typeQuestion; // "multiple", "single", "truefalse", "text"

    @NotNull(message = "Le nombre de points est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le nombre de points doit être positif")
    @DecimalMax(value = "100.0", message = "Le nombre de points ne peut excéder 100")
    private Integer points;

    // @NotBlank(message = "La difficulté est obligatoire")
    // private String difficulte; // "easy", "medium", "hard"

    
    private LocalTime time;

    @Valid
    private List<ReponseRequestDTO> reponses;
}