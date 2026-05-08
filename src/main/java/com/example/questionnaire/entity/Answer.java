package com.example.questionnaire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private Response response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // The actual typed/chosen answer
    // OPEN_TEXT    → the typed text
    // SINGLE_CHOICE → the selected option string e.g. "Friend"
    // MULTI_CHOICE  → comma-joined selections e.g. "Social Media,Friend"
    // RATING        → the number as string e.g. "4"
    @Column(columnDefinition = "TEXT")
    private String value;

    // 1-5 rating that accompanies EVERY question regardless of type
    @Column(nullable = false)
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}