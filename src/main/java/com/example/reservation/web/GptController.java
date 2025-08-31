package com.example.reservation.web;

import com.example.reservation.AiQuestion;
import com.example.reservation.AiQuestionRepository;
import com.example.reservation.service.GptService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;
    private final AiQuestionRepository aiQuestionRepository;

    public GptController(GptService gptService, AiQuestionRepository aiQuestionRepository) {
        this.gptService = gptService;
        this.aiQuestionRepository = aiQuestionRepository;
    }

    @GetMapping("/ask")
    public String ask(Authentication authentication, @RequestParam("q") String q) {
        // JWT 필터에서 넣어준 사용자 이메일
        String userEmail = authentication.getName();
        return gptService.askAndSave(userEmail, q);
    }

    // 🆕 질문/답변 히스토리 조회
    @GetMapping("/history")
    public List<AiQuestion> history(Authentication authentication) {
        String userEmail = authentication.getName();
        return aiQuestionRepository.findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

}