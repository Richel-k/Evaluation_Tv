package com.Application.EvaluationTV.repository;


import com.Application.EvaluationTV.model.Examinateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExaminateurRepository extends JpaRepository<Examinateur, Long> {
    Optional<Examinateur> findByEmail(String email);

    boolean existsByEmail(String email);
}
