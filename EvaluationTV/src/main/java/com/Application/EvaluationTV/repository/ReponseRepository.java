package com.Application.EvaluationTV.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Application.EvaluationTV.model.OptionReponse;
import com.Application.EvaluationTV.model.ReponseCandidat;

public interface ReponseRepository extends JpaRepository<OptionReponse, Long> {
    
}
