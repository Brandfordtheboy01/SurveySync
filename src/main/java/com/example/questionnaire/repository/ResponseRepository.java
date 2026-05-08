package com.example.questionnaire.repository;

import com.example.questionnaire.entity.Response;
import com.example.questionnaire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    Optional<Response> findByUserAndStatus(User user, Response.Status status);

    List<Response> findByStatusOrderBySubmittedAtDesc(Response.Status status);

    List<Response> findByStatusAndSubmittedAtBetween(
            Response.Status status,
            LocalDateTime from,
            LocalDateTime to
    );

    long countByStatus(Response.Status status);
}