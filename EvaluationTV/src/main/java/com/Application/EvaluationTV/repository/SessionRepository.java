package com.Application.EvaluationTV.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Application.EvaluationTV.model.Candidat;
import com.Application.EvaluationTV.model.Question;
import com.Application.EvaluationTV.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByCode(String codeSession);

    @Query(value = """
            select q.id, q.enonce, q.points, q.time, q.type, q.session_id
            from questions q
            join sessions s on s.id = q.session_id
            where s.code = :sessionId

            """, nativeQuery = true)
    List<Question> AllQuestionOfMe(@Param("sessionId") String sessionId);

    @Query(
        value = """
                select sum(q.points) from questions q
                join sessions s on s.id = q.session_id
                where s.code = :code_session
                """, nativeQuery = true
    )
    Integer GetAllPoints(@Param("code_session") String id_session);


    @Query(value = """
            select s.id, s.code, s.titre, s.statut, count(q.id) as nombre_question, sum(q.points) as points, s.date_debut, s.heure_debut
            from sessions s
            join questions q on q.session_id = s.id
            where s.examinateur_id = :examinateur_id
            group by s.id, s.titre, s.code, s.statut, s.date_debut, s.heure_debut
            """, nativeQuery = true)
    List<Object[]> GetMySessions(@Param("examinateur_id") Long examinateur_id);
}
