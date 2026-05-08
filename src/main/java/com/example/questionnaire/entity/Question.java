package com.example.questionnaire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Question text cannot be empty")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    // Stores choices as comma-separated string e.g. "Social Media,Search Engine,Friend,Other"
    // Null for OPEN_TEXT and RATING types
    @Column(columnDefinition = "TEXT")
    private String options;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private int displayOrder = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum QuestionType {
        OPEN_TEXT,
        SINGLE_CHOICE,
        MULTI_CHOICE,
        RATING
    }
}