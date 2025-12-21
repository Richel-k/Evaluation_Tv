package com.Application.EvaluationTV.repository;

import com.Application.EvaluationTV.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    Optional<Candidat> findByEmail(String email);

    // Optional<Candidat> findByMatricule(String matricule);

    boolean existsByEmail(String email);

    // @Query(value = """
    // SELECT
    // s.code,
    // s.titre ,
    // SUM(r.points_obtenus) AS total_points
    // FROM sessions s
    // INNER JOIN reponses_candidat r ON s.id = r.session_id -- ou la clé de
    // relation appropriée
    // WHERE r.candidat_id = :id_candidate
    // GROUP BY s.code, s.titre
    // ORDER BY total_points DESC;
    // """, nativeQuery = true)
    @Query(value = """
            SELECT 
                s.code AS session_code,
                s.titre AS session_titre,
                s.date_debut,
                SUM(rc.points_obtenus) AS total_points
            FROM sessions s
            INNER JOIN questions q ON s.id = q.session_id
            INNER JOIN reponses_candidat rc ON q.id = rc.question_id
            WHERE rc.candidat_id = :id_candidate
            GROUP BY s.code, s.titre
            ORDER BY total_points DESC;
            """, nativeQuery = true)
    List<Object[]>  giveMyResult(@Param("id_candidate") Long identifier);
}