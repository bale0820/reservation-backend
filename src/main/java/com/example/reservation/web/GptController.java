package com.example.reservation.web;

import com.example.reservation.entity.AiQuestion;
import com.example.reservation.repository.AiQuestionRepository;
import com.example.reservation.service.GptService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;
    private final AiQuestionRepository aiQuestionRepository;

    public GptController(GptService gptService,
                         AiQuestionRepository aiQuestionRepository) {
        this.gptService = gptService;
        this.aiQuestionRepository = aiQuestionRepository;
    }

    @GetMapping("/ask")
    public String ask(Authentication authentication,
                      @RequestParam("q") String q) {

        String userEmail = authentication.getName();
        return gptService.askAndSave(userEmail, q);
    }

    @GetMapping("/history")
    public List<AiQuestion> history(Authentication authentication) {
        String userEmail = authentication.getName();
        return aiQuestionRepository
                .findByUserEmailOrderByCreatedAtDesc(userEmail);
    }
}
