package com.Application.EvaluationTV.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Application.EvaluationTV.model.OptionReponse;
import com.Application.EvaluationTV.model.Question;
import com.Application.EvaluationTV.model.ReponseCandidat;
import com.Application.EvaluationTV.dto.*;

public interface ReponseRepository extends JpaRepository<OptionReponse, Long> {
    @Query(value = """
            select r.id, r.texte, r.est_correcte
            from options_reponse r
            join questions q on q.id = r.question_id
            where q.id = :questionId
            """, nativeQuery = true)
    List<ReponseRequestDTO> AllResponseOfMe(@Param("questionId") Long questionId);

    @Query(value = """
            select est_correcte from
            """, nativeQuery = true)
    Integer Decision(@Param("questionId") Long questionId, @Param("answerId") Long answerId);
}
