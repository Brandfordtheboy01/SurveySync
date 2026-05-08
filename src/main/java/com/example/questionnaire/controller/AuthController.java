package com.example.questionnaire.controller;

import com.example.questionnaire.dto.RegisterRequest;
import com.example.questionnaire.entity.User;
import com.example.questionnaire.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        try {
            User user = userService.register(
                    req.getName(), req.getEmail(), req.getPassword());
            return ResponseEntity.status(201).body(Map.of(
                    "message", "Account created successfully",
                    "id",      user.getId(),
                    "email",   user.getEmail(),
                    "role",    user.getRole().name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Spring Security validates credentials before this runs
    // Wrong credentials → automatic 401, this method never executes
    @GetMapping("/login")
    public ResponseEntity<?> login(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "id",      user.getId(),
                "name",    user.getName(),
                "email",   user.getEmail(),
                "role",    user.getRole().name()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(Map.of(
                "id",    user.getId(),
                "name",  user.getName(),
                "email", user.getEmail(),
                "role",  user.getRole().name()
        ));
    }
}