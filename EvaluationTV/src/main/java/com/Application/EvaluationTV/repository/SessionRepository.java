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

    
}
