package com.example.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

@Configuration
public class OpenAIConfig {
    @Bean
    public OpenAIClient openAIClient() {
        // 환경변수 OPENAI_API_KEY 를 자동으로 읽습니다.
        return OpenAIOkHttpClient.fromEnv();
    }
}
