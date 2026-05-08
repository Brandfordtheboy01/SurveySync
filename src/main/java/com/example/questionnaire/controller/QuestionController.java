package com.example.questionnaire.controller;

import com.example.questionnaire.dto.QuestionDto;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // Frontend calls this to render the form
    @GetMapping
    public List<Question> getActive() {
        return questionService.getActiveQuestions();
    }

    // Manager sees all including inactive
    @GetMapping("/all")
    public List<Question> getAll() {
        return questionService.getAllQuestions();
    }

    // Manager creates a question
    @PostMapping("/create")
    public ResponseEntity<Question> create(@RequestBody @Valid QuestionDto dto) {
        Question q = questionService.createQuestion(
                dto.getText(), dto.getType(),
                dto.getOptions(), dto.getDisplayOrder());
        return ResponseEntity.status(201).body(q);
    }

    // Manager edits a question
    @PutMapping("/{id}/edit")
    public Question edit(@PathVariable Long id,
                         @RequestBody @Valid QuestionDto dto) {
        return questionService.updateQuestion(
                id, dto.getText(), dto.getOptions(),
                dto.getDisplayOrder(), dto.isActive());
    }

    // Manager soft-deletes — sets active=false
    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        questionService.deactivateQuestion(id);
        return ResponseEntity.noContent().build();
    }
}