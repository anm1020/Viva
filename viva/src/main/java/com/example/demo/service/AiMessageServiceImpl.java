package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.AiMessage;
import com.example.demo.repository.AiMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiMessageServiceImpl implements AiMessageService {

    private final AiMessageRepository aiMessageRepository;
    private final AIService aiService;
    private final AiSessionService aiSessionService;
    

    @Override
    public AiMessage saveMessage(AiMessage message) {
        return aiMessageRepository.save(message);
    }

    @Override
    public List<AiMessage> getMessagesBySessionId(String sessionId) {
        return aiMessageRepository.findBySessionIdOrderByCreatedDtAsc(sessionId);
    }

    @Override
    public Map<String, AiMessage> sendAndReply(String sessionId, String prompt) {
        // ✅ 1. 사용자 메시지 저장
        AiMessage userMessage = saveMessage(AiMessage.builder()
            .sessionId(sessionId)
            .role("user")
            .content(prompt)
            .createdDt(LocalDateTime.now().toString())
            .build());

        // ✅ 2. GPT API 호출
        String response = aiService.askChatGPT(prompt);

        // ✅ 3. GPT 응답 저장
        AiMessage assistantMessage = saveMessage(AiMessage.builder()
            .sessionId(sessionId)
            .role("assistant")
            .content(response)
            .createdDt(LocalDateTime.now().toString())
            .build());

        // ✅ 4. 메시지 6개 이상일 경우 자동 요약
        List<AiMessage> allMessages = getMessagesBySessionId(sessionId);
        if (allMessages.size() >= 6) {
            try {
                String summary = aiService.summarizeMessages(allMessages);
                aiSessionService.updateSummary(sessionId, summary);
            } catch (Exception e) {
                System.err.println("요약 생성 중 오류: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // ✅ 5. 결과 반환
        return Map.of(
            "userMessage", userMessage,
            "assistantMessage", assistantMessage
        );
    }

    @Override
    public void deleteMessagesBySessionId(String sessionId) {
        aiMessageRepository.deleteAll(aiMessageRepository.findBySessionIdOrderByCreatedDtAsc(sessionId));
    }
    
    @Override
    public AiMessage sendAndSaveAiReply(String sessionId, String userText) {
        // 1. GPT 호출
        String gptReply = aiService.askChatGPT(userText); // 의존성 주입된 AIService 사용

        // 2. 저장
        AiMessage aiMsg = new AiMessage();
        aiMsg.setMessageId(UUID.randomUUID().toString());
        aiMsg.setSessionId(sessionId);
        aiMsg.setRole("ai");
        aiMsg.setContent(gptReply);
        aiMsg.setCreatedDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return aiMessageRepository.save(aiMsg);
    }


}
