// SessionResponseDTO.java
package com.Application.EvaluationTV.dto;

import com.Application.EvaluationTV.model.Enum.StatutSession;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionResponseDTO {
    private Long id;
    private String titre;
    private String codeSession;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateSession;

    private Integer dureeMinutes;
    private StatutSession statut;
    private Integer nombreQuestions;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateDebut;

    private String examinateurNom;
    private String examinateurEmail;
}