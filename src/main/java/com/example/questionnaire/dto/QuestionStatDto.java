package com.example.questionnaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionStatDto {
    private Long   questionId;
    private String questionText;
    private String questionType;
    private int    responseCount;
    private double averageRating;
}