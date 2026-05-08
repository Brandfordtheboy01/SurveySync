package com.example.questionnaire.config;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.User;
import com.example.questionnaire.repository.QuestionRepository;
import com.example.questionnaire.repository.UserRepository;
import com.example.questionnaire.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Public
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Manager only
                        .requestMatchers("/api/manager/**").hasRole("MANAGER")
                        .requestMatchers("/api/questions/create").hasRole("MANAGER")
                        .requestMatchers("/api/questions/*/edit").hasRole("MANAGER")
                        .requestMatchers("/api/questions/*/deactivate").hasRole("MANAGER")
                        .requestMatchers("/api/questions/all").hasRole("MANAGER")

                        // Any authenticated user
                        .requestMatchers("/api/questions").authenticated()
                        .requestMatchers("/api/responses/**").authenticated()
                        .requestMatchers("/api/auth/me").authenticated()

                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userService)
                .headers(h -> h.frameOptions(f -> f.disable()));

        return http.build();
    }

    // ── Seed manager account + 10 questions on startup ───────────────────────
    @Bean
    public CommandLineRunner seedData(UserRepository userRepo,
                                      QuestionRepository questionRepo,
                                      PasswordEncoder encoder) {
        return args -> {

            // 1. Seed manager account
            if (!userRepo.existsByEmail("manager@company.com")) {
                User manager = new User();
                manager.setName("Default Manager");
                manager.setEmail("manager@company.com");
                manager.setPassword(encoder.encode("Manager@123"));
                manager.setRole(User.Role.MANAGER);
                manager.setEnabled(true);
                userRepo.save(manager);
                System.out.println("=== Manager seeded: manager@company.com / Manager@123 ===");
            }

            // 2. Seed 10 questions exactly matching the frontend
            //    Frontend fetches these — nothing is hardcoded there
            if (questionRepo.count() == 0) {

                // Q1 — open text (short input)
                save(questionRepo, 1,
                        "What is your full name?",
                        Question.QuestionType.OPEN_TEXT, null);

                // Q2 — single choice radio
                save(questionRepo, 2,
                        "How did you hear about us?",
                        Question.QuestionType.SINGLE_CHOICE,
                        "Social Media,Search Engine,Friend,Other");

                // Q3 — multi choice checkboxes
                save(questionRepo, 3,
                        "Which products have you used?",
                        Question.QuestionType.MULTI_CHOICE,
                        "Product A,Product B,Product C");

                // Q4 — rating question (value and rating will both be the number chosen)
                save(questionRepo, 4,
                        "Rate your overall satisfaction (1-5):",
                        Question.QuestionType.RATING, null);

                // Q5 — rating question
                save(questionRepo, 5,
                        "Rate the quality of our support (1-5):",
                        Question.QuestionType.RATING, null);

                // Q6 — single choice radio
                save(questionRepo, 6,
                        "Would you recommend us to a friend?",
                        Question.QuestionType.SINGLE_CHOICE,
                        "Yes,No,Maybe");

                // Q7 — multi choice checkboxes
                save(questionRepo, 7,
                        "Which features do you find most valuable?",
                        Question.QuestionType.MULTI_CHOICE,
                        "Feature X,Feature Y,Feature Z");

                // Q8 — rating question
                save(questionRepo, 8,
                        "Rate the ease of use of our product (1-5):",
                        Question.QuestionType.RATING, null);

                // Q9 — open text (textarea)
                save(questionRepo, 9,
                        "What could we improve?",
                        Question.QuestionType.OPEN_TEXT, null);

                // Q10 — open text (textarea)
                save(questionRepo, 10,
                        "Any additional comments?",
                        Question.QuestionType.OPEN_TEXT, null);

                System.out.println("=== 10 questions seeded ===");
            }
        };
    }

    // Helper to build and save a question
    private void save(QuestionRepository repo, int order, String text,
                      Question.QuestionType type, String options) {
        Question q = new Question();
        q.setDisplayOrder(order);
        q.setText(text);
        q.setType(type);
        q.setOptions(options);
        q.setActive(true);
        repo.save(q);
    }
}