package com.example.reservation.service;

import com.example.reservation.AiQuestion;
import com.example.reservation.AiQuestionRepository;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.stereotype.Service;

@Service
public class GptService {

    private final OpenAIClient client;
    private final AiQuestionRepository aiQuestionRepository;

    public GptService(OpenAIClient client, AiQuestionRepository aiQuestionRepository) {
        this.client = client;
        this.aiQuestionRepository = aiQuestionRepository;
    }

    public String askAndSave(String userEmail, String userMessage) {
        try {
            // 1) GPT 호출
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model("gpt-3.5-turbo")  // 안전하게 접근 가능한 모델
                    .addUserMessage(userMessage)
                    .build();

            ChatCompletion chat = client.chat().completions().create(params);
            String answer = chat.choices().get(0).message().content().orElse("응답 없음");

            // 2) DB 저장
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
}
