package com.example.questionnaire.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AnswerDto {

    @NotNull(message = "questionId is required")
    private Long questionId;

    // The typed text or selected option(s)
    // For MULTI_CHOICE send comma-joined: "Social Media,Friend"
    private String value;

    // Required for every question — always 1 to 5
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;
}