package com.example.questionnaire.repository;

import com.example.questionnaire.entity.Answer;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByResponse(Response response);
    List<Answer> findByQuestion(Question question);
}