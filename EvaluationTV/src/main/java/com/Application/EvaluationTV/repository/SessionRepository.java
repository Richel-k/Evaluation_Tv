package com.Application.EvaluationTV.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Application.EvaluationTV.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long>{
    
}
