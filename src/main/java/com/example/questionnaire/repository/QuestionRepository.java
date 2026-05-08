package com.example.questionnaire.repository;

import com.example.questionnaire.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Used by users filling the form — ordered by displayOrder
    List<Question> findByActiveTrueOrderByDisplayOrderAsc();

    // Used by manager to see all including inactive
    List<Question> findAllByOrderByDisplayOrderAsc();

    long countByActiveTrue();
}