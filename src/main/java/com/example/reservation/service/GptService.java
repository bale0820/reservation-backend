package com.example.reservation.service;

import com.example.reservation.entity.AiQuestion;
import com.example.reservation.repository.AiQuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GptService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final AiQuestionRepository aiQuestionRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public GptService(AiQuestionRepository aiQuestionRepository) {
        this.aiQuestionRepository = aiQuestionRepository;
    }

    public String askAndSave(String userEmail, String userMessage) {

        try {
            // 1️⃣ OpenAI 요청
            String answer = callOpenAI(userMessage);

            // 2️⃣ DB 저장
            AiQuestion aiQuestion = new AiQuestion();
            aiQuestion.setUserEmail(userEmail);
            aiQuestion.setQuestion(userMessage);
            aiQuestion.setAnswer(answer);

            aiQuestionRepository.save(aiQuestion);

            return answer;

        } catch (Exception e) {
            return "[ERROR] GPT 호출 실패: " + e.getMessage();
        }
    }

    private String callOpenAI(String userMessage) {

        String url = "https://api.openai.com/v1/responses";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", "gpt-4.1-mini",
                "input", new Object[]{
                        Map.of(
                                "role", "user",
                                "content", new Object[]{
                                        Map.of(
                                                "type", "input_text",
                                                "text", userMessage
                                        )
                                }
                        )
                }
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, entity, Map.class);

        // output → content → text 추출
        Map responseBody = response.getBody();

        List output = (List) responseBody.get("output");
        Map message = (Map) output.get(0);
        List content = (List) message.get("content");
        Map textObj = (Map) content.get(0);

        return textObj.get("text").toString();
    }
}
