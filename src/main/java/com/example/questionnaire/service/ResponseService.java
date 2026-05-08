package com.example.questionnaire.service;

import com.example.questionnaire.dto.AnswerDto;
import com.example.questionnaire.entity.*;
import com.example.questionnaire.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final AnswerRepository   answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository     userRepository;

    // Start new questionnaire or resume existing draft
    public Response getOrCreateDraft(String email) {
        User user = findUser(email);

        Optional<Response> existing =
                responseRepository.findByUserAndStatus(user, Response.Status.DRAFT);
        if (existing.isPresent()) {
            return existing.get();          // resume
        }

        Response draft = new Response();
        draft.setUser(user);
        draft.setStatus(Response.Status.DRAFT);
        return responseRepository.save(draft);
    }

    // Save partial answers — draft stays DRAFT
    public Response saveAnswers(String email, Long responseId,
                                List<AnswerDto> dtos) {
        Response response = getOwnedDraft(email, responseId);

        for (AnswerDto dto : dtos) {
            Question question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() ->
                            new RuntimeException("Question not found: " + dto.getQuestionId()));

            if (!question.isActive()) {
                throw new IllegalArgumentException(
                        "Cannot answer inactive question id: " + dto.getQuestionId());
            }

            // Update existing answer for this question or create a new one
            Answer answer = answerRepository.findByResponse(response)
                    .stream()
                    .filter(a -> a.getQuestion().getId().equals(dto.getQuestionId()))
                    .findFirst()
                    .orElse(new Answer());

            answer.setResponse(response);
            answer.setQuestion(question);
            answer.setValue(dto.getValue());
            answer.setRating(dto.getRating());
            answerRepository.save(answer);
        }

        return responseRepository.save(response);
    }

    // Submit — validates all questions answered, locks the form
    public Response submit(String email, Long responseId) {
        Response response = getOwnedDraft(email, responseId);

        List<Question> activeQuestions =
                questionRepository.findByActiveTrueOrderByDisplayOrderAsc();
        List<Answer> currentAnswers = answerRepository.findByResponse(response);

        Set<Long> answeredIds = currentAnswers.stream()
                .map(a -> a.getQuestion().getId())
                .collect(Collectors.toSet());

        // Check every active question has been answered
        for (Question q : activeQuestions) {
            if (!answeredIds.contains(q.getId())) {
                throw new IllegalStateException(
                        "Question " + q.getId() + " has not been answered: \""
                                + q.getText() + "\"");
            }
        }

        response.setStatus(Response.Status.SUBMITTED);
        response.setSubmittedAt(LocalDateTime.now());
        return responseRepository.save(response);
    }

    // User views their own submitted response
    public Response getOwnSubmission(String email) {
        User user = findUser(email);
        return responseRepository
                .findByUserAndStatus(user, Response.Status.SUBMITTED)
                .orElseThrow(() ->
                        new RuntimeException("You have no submitted response yet"));
    }

    // Manager views all submissions
    public List<Response> getAllSubmissions() {
        return responseRepository
                .findByStatusOrderBySubmittedAtDesc(Response.Status.SUBMITTED);
    }

    // Manager filters by date range
    public List<Response> getByDateRange(LocalDateTime from, LocalDateTime to) {
        return responseRepository.findByStatusAndSubmittedAtBetween(
                Response.Status.SUBMITTED, from, to);
    }

    public Response getById(Long id) {
        return responseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Response not found: " + id));
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + email));
    }

    private Response getOwnedDraft(String email, Long responseId) {
        Response response = responseRepository.findById(responseId)
                .orElseThrow(() ->
                        new RuntimeException("Response not found: " + responseId));

        if (!response.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You do not own this response");
        }
        if (response.getStatus() == Response.Status.SUBMITTED) {
            throw new IllegalStateException(
                    "This response is already submitted and cannot be changed");
        }
        return response;
    }
}