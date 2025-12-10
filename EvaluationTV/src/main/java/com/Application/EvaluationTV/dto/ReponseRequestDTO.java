package com.Application.EvaluationTV.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReponseRequestDTO {

    @NotBlank(message = "Le texte de la réponse est obligatoire")
    @Size(min = 1, max = 500, message = "Le texte de la réponse doit contenir entre 1 et 500 caractères")
    private String texte;

    @NotNull(message = "La validité de la réponse est obligatoire")
    private Boolean correcte;

    // @NotNull(message = "L'ordre de la réponse est obligatoire")
    // private Integer ordre;
}