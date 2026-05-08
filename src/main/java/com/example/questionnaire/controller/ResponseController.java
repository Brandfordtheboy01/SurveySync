package com.example.questionnaire.controller;

import com.example.questionnaire.dto.AnswerDto;
import com.example.questionnaire.entity.Response;
import com.example.questionnaire.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/responses")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    // Start or resume draft
    @PostMapping("/start")
    public Response start(Authentication auth) {
        return responseService.getOrCreateDraft(auth.getName());
    }

    // Save partial answers
    @PutMapping("/{id}/save")
    public Response save(@PathVariable Long id,
                         @RequestBody List<AnswerDto> answers,
                         Authentication auth) {
        return responseService.saveAnswers(auth.getName(), id, answers);
    }

    // Final submit
    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submit(@PathVariable Long id,
                                    Authentication auth) {
        try {
            Response r = responseService.submit(auth.getName(), id);
            return ResponseEntity.ok(Map.of(
                    "message",     "Questionnaire submitted successfully",
                    "responseId",  r.getId(),
                    "submittedAt", r.getSubmittedAt().toString()
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // User views their own submission
    @GetMapping("/mine")
    public Response mine(Authentication auth) {
        return responseService.getOwnSubmission(auth.getName());
    }
}