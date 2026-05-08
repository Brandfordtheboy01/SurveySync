package com.example.questionnaire.controller;

import com.example.questionnaire.dto.SummaryDto;
import com.example.questionnaire.entity.Response;
import com.example.questionnaire.service.ResponseService;
import com.example.questionnaire.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ResponseService responseService;
    private final SummaryService  summaryService;

    // All submissions — optional ?from=...&to=... date filter
    @GetMapping("/responses")
    public List<Response> all(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        if (from != null && to != null) {
            return responseService.getByDateRange(
                    LocalDateTime.parse(from), LocalDateTime.parse(to));
        }
        return responseService.getAllSubmissions();
    }

    // One specific response in full detail
    @GetMapping("/responses/{id}")
    public Response one(@PathVariable Long id) {
        return responseService.getById(id);
    }

    // Dashboard: total counts + per-question average ratings
    @GetMapping("/summary")
    public SummaryDto summary() {
        return summaryService.buildSummary();
    }
}