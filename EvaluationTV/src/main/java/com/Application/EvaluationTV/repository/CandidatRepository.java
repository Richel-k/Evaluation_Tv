package com.Application.EvaluationTV.repository;

import com.Application.EvaluationTV.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    Optional<Candidat> findByEmail(String email);

    //Optional<Candidat> findByMatricule(String matricule);

    boolean existsByEmail(String email);
}