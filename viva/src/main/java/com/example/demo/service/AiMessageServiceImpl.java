package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.AiMessage;
import com.example.demo.repository.AiMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiMessageServiceImpl implements AiMessageService {

    private final AiMessageRepository aiMessageRepository;
    private final AIService aiService;

    @Override
    public AiMessage saveMessage(AiMessage message) { 
        return aiMessageRepository.save(message);
    }
    
    @Override
    public List<AiMessage> getMessagesBySessionId(String sessionId) {
        try {
            if (sessionId == null || sessionId.trim().isEmpty()) {
                return List.of();
            }
            
            List<AiMessage> messages = aiMessageRepository.findBySessionIdOrderByCreatedDtAsc(sessionId);
            return messages != null ? messages : List.of();
        } catch (Exception e) {
            System.err.println("메시지 조회 중 서비스 오류: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    	
    @Override
    public Map<String, AiMessage> sendAndReply(String sessionId, String userPrompt) {
        try {
            // 1. 사용자 질문 저장
            AiMessage userMessage = AiMessage.builder()
                    .sessionId(sessionId)
                    .role("user")
                    .content(userPrompt)
                    .createdDt(java.time.LocalDateTime.now().toString())
                    .build();
            aiMessageRepository.save(userMessage);

            // 2. GPT 응답
            String gptResponse = aiService.askChatGPT(userPrompt);

            // 3. GPT 메시지 저장
            AiMessage assistantMessage = AiMessage.builder()
                    .sessionId(sessionId)
                    .role("assistant")
                    .content(gptResponse)
                    .createdDt(java.time.LocalDateTime.now().toString())
                    .build();
            aiMessageRepository.save(assistantMessage);

            // 4. 묶어서 리턴
            return Map.of(
                    "userMessage", userMessage,
                    "assistantMessage", assistantMessage
            );
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 기본 응답 생성
            AiMessage errorMessage = AiMessage.builder()
                    .sessionId(sessionId)
                    .role("assistant")
                    .content("죄송합니다. 응답을 생성하는 중 오류가 발생했습니다: " + e.getMessage())
                    .createdDt(java.time.LocalDateTime.now().toString())
                    .build();
            aiMessageRepository.save(errorMessage);
            
            return Map.of(
                    "userMessage", null,
                    "assistantMessage", errorMessage
            );
        }
    }
}
