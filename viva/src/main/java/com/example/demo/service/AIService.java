package com.example.demo.service;

import com.example.demo.config.AIConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AIService {

    private final AIConfig AIConfig;

    public String askChatGPT(String userPrompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AIConfig.getApiKey());

        Map<String, Object> body = new HashMap<>();
        body.put("model", AIConfig.getModel());

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "너는 엄격한 면접관 역할을 해야 해."));
        messages.add(Map.of("role", "user", "content", userPrompt));
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                AIConfig.getApiUrl(), request, Map.class
            );

            if (response.getBody() == null) {
                return "API 응답이 비어있습니다.";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices == null || choices.isEmpty()) {
                return "API 응답에서 choices를 찾을 수 없습니다.";
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            if (message == null) {
                return "API 응답에서 message를 찾을 수 없습니다.";
            }

            String content = (String) message.get("content");
            if (content == null) {
                return "API 응답에서 content를 찾을 수 없습니다.";
            }

            return content;

        } catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }
}
