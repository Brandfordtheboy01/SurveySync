package com.example.questionnaire.dto;

import com.example.questionnaire.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionDto {

    @NotBlank(message = "Question text is required")
    private String text;

    @NotNull(message = "Question type is required — OPEN_TEXT, SINGLE_CHOICE, MULTI_CHOICE, RATING")
    private Question.QuestionType type;

    // Required only when type is SINGLE_CHOICE or MULTI_CHOICE
    // Send as comma-separated: "Social Media,Search Engine,Friend,Other"
    private String options;

    private int displayOrder;
    private boolean active = true;
}