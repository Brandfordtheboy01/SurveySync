package com.example.questionnaire.service;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // Called by frontend to render the form — active only, in order
    public List<Question> getActiveQuestions() {
        return questionRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    // Manager sees everything including inactive
    public List<Question> getAllQuestions() {
        return questionRepository.findAllByOrderByDisplayOrderAsc();
    }

    // Manager creates a new question
    public Question createQuestion(String text, Question.QuestionType type,
                                   String options, int displayOrder) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Question text cannot be empty");
        }
        if ((type == Question.QuestionType.SINGLE_CHOICE ||
                type == Question.QuestionType.MULTI_CHOICE)
                && (options == null || options.isBlank())) {
            throw new IllegalArgumentException(
                    "SINGLE_CHOICE and MULTI_CHOICE questions must have options " +
                            "e.g. \"Option A,Option B,Option C\"");
        }
        Question q = new Question();
        q.setText(text.trim());
        q.setType(type);
        q.setOptions(options);
        q.setDisplayOrder(displayOrder);
        q.setActive(true);
        return questionRepository.save(q);
    }

    // Manager edits a question
    public Question updateQuestion(Long id, String text, String options,
                                   int displayOrder, boolean active) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question not found with id: " + id));
        q.setText(text.trim());
        q.setOptions(options);
        q.setDisplayOrder(displayOrder);
        q.setActive(active);
        return questionRepository.save(q);
    }

    // Soft delete — sets active=false, old answers still make sense
    public void deactivateQuestion(Long id) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question not found with id: " + id));
        q.setActive(false);
        questionRepository.save(q);
    }

    public Question getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question not found with id: " + id));
    }
}