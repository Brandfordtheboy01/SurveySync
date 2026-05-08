package com.example.questionnaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SummaryDto {
    private long   totalSubmitted;
    private long   totalDrafts;
    private double completionRatePercent;
    private List<QuestionStatDto> questionStats;
}