package com.example.questionnaire.service;

import com.example.questionnaire.dto.QuestionStatDto;
import com.example.questionnaire.dto.SummaryDto;
import com.example.questionnaire.entity.Answer;
import com.example.questionnaire.entity.Response;
import com.example.questionnaire.repository.AnswerRepository;
import com.example.questionnaire.repository.QuestionRepository;
import com.example.questionnaire.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository   answerRepository;

    public SummaryDto buildSummary() {
        long submitted = responseRepository.countByStatus(Response.Status.SUBMITTED);
        long drafts    = responseRepository.countByStatus(Response.Status.DRAFT);

        double completionRate = (submitted + drafts) == 0 ? 0.0
                : (double) submitted / (submitted + drafts) * 100;

        List<QuestionStatDto> questionStats = questionRepository
                .findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(q -> {
                    List<Answer> answers = answerRepository.findByQuestion(q);
                    double avgRating = answers.stream()
                            .mapToInt(Answer::getRating)
                            .average()
                            .orElse(0.0);
                    return new QuestionStatDto(
                            q.getId(),
                            q.getText(),
                            q.getType().name(),
                            answers.size(),
                            Math.round(avgRating * 10.0) / 10.0
                    );
                })
                .toList();

        return new SummaryDto(
                submitted,
                drafts,
                Math.round(completionRate * 10.0) / 10.0,
                questionStats
        );
    }
}