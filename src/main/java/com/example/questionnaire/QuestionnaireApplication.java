package com.example.questionnaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuestionnaireApplication {

	public static void main(String[] args) {

        SpringApplication.run(QuestionnaireApplication.class, args);
	}

}
