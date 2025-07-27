package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.config.AIConfig;
import com.example.demo.model.entity.AiMessage;

import lombok.RequiredArgsConstructor;

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

            if (response.getBody() == null) return "API 응답이 비어있습니다.";
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices == null || choices.isEmpty()) return "API 응답에서 choices를 찾을 수 없습니다.";
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            if (message == null) return "API 응답에서 message를 찾을 수 없습니다.";

            String content = (String) message.get("content");
            if (content == null) return "API 응답에서 content를 찾을 수 없습니다.";
            return content;

        } catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }

    // ✅ 요약 메서드 추가
    public String summarizeMessages(List<AiMessage> messages) {
        StringBuilder sb = new StringBuilder();
        for (AiMessage msg : messages) {
            sb.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }

        String summaryPrompt = "다음 면접 대화를 요약해줘. 면접자의 답변 흐름과 주요 포인트를 간단히 정리해줘:\n" + sb.toString();
        return askChatGPT(summaryPrompt);
    }
    
    public String requestSpeechFeedback(String whisperText) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AIConfig.getApiKey());

        List<Map<String, String>> messages = List.of(
            Map.of("role", "system", "content", "너는 사용자의 발표를 평가하는 AI 피드백 분석가야. 사용자의 발화를 듣고 말속도, 발음, 전체 내용 요약, 장점, 단점을 텍스트로 평가해줘."),
            Map.of("role", "user", "content", whisperText)
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", AIConfig.getModel());
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
        		AIConfig.getApiUrl(),
            request,
            Map.class
        );

        Map<String, Object> resBody = response.getBody();
        if (resBody != null) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) resBody.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        }
        return null;
    }

}
 