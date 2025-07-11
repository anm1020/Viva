package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.AiVoiceDTO;
import com.example.demo.model.entity.AiVoice;
import com.example.demo.repository.AiVoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiVoiceService {

    private final AiVoiceRepository aiVoiceRepository;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    // 저장
    public void saveVoiceFeedback(String userId, AiVoiceDTO dto) {
        try {
            AiVoice voice = new AiVoice();
            voice.setUserId(userId);
            voice.setSessionId(dto.getSessionId());
            voice.setTranscript(dto.getTranscript());
            voice.setSummary(dto.getSummary());
            voice.setDetailsJson(objectMapper.writeValueAsString(dto.getDetails()));
            voice.setSpeechSpeed(dto.getSpeechSpeed());
            voice.setPronunciation(dto.getPronunciation());
            voice.setStrengths(dto.getStrengths());
            voice.setWeaknesses(dto.getWeaknesses());
            voice.setExampleSentence(dto.getExampleSentence());
            voice.setSuggestionsJson(objectMapper.writeValueAsString(dto.getSuggestions()));
            voice.setCreatedAt(LocalDateTime.now());

            aiVoiceRepository.save(voice);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 오류", e);
        }
    }

    // 조회
    public List<AiVoice> getVoiceFeedbackList(String userId) {
        return aiVoiceRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Whisper API 호출 통합
    public String transcribe(MultipartFile file) {
        try {
            String url = "https://api.openai.com/v1/audio/transcriptions";

            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);
            body.add("model", "whisper-1");
            body.add("language", "ko");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(openAiApiKey);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return (String) response.getBody().get("text");
            } else {
                throw new RuntimeException("Whisper API 호출 실패");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Whisper 변환 중 오류: " + e.getMessage());
        }
    }

    // JSON 파싱 메서드들
    public List<AiVoiceDTO.FeedbackItem> parseDetails(String detailsJson) {
        try {
            if (detailsJson == null || detailsJson.trim().isEmpty()) {
                return List.of();
            }
            return objectMapper.readValue(detailsJson, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, AiVoiceDTO.FeedbackItem.class));
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<String> parseSuggestions(String suggestionsJson) {
        try {
            if (suggestionsJson == null || suggestionsJson.trim().isEmpty()) {
                return List.of();
            }
            return objectMapper.readValue(suggestionsJson, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (Exception e) {
            return List.of();
        }
    }

    // 대화방 관련 메서드들
    public String createNewConversation(String userId) {
        String conversationId = "conv_" + java.util.UUID.randomUUID().toString().replace("-", "");
        return conversationId;
    }

    public List<String> getUserConversationIds(String userId) {
        return aiVoiceRepository.findDistinctConversationIdByUserId(userId);
    }

    public List<AiVoice> getConversationMessages(String conversationId) {
        return aiVoiceRepository.findByConversationIdOrderByMessageOrderAsc(conversationId);
    }

    public void saveVoiceMessage(String userId, String conversationId, String transcript, String feedback) {
        int nextOrder = aiVoiceRepository.countByConversationId(conversationId) + 1;
        
        AiVoice voice = new AiVoice();
        voice.setUserId(userId);
        voice.setConversationId(conversationId);
        voice.setMessageOrder(nextOrder);
        voice.setTranscript(transcript);
        voice.setSummary(feedback);
        voice.setSpeechSpeed("적절함");
        voice.setPronunciation("명확함");
        voice.setStrengths("자신감 있게 답변");
        voice.setWeaknesses("개선 여지 있음");
        voice.setRole("user");
        voice.setCreatedAt(java.time.LocalDateTime.now());
        
        aiVoiceRepository.save(voice);
    }

    // AI 면접관 질문 목록
    private static final String[] INTERVIEW_QUESTIONS = {
        "안녕하세요. 자기소개 부탁드립니다.",
        "지원하신 직무에 대한 경험을 말씀해주세요.",
        "가장 기억에 남는 프로젝트는 무엇인가요?",
        "팀워크 경험에 대해 말씀해주세요.",
        "본인의 장단점에 대해 말씀해주세요.",
        "앞으로의 계획이나 목표에 대해 말씀해주세요."
    };

    // AI 질문 저장
    public void saveAiQuestion(String userId, String conversationId, String question, int order) {
        AiVoice voice = new AiVoice();
        voice.setUserId(userId);
        voice.setConversationId(conversationId);
        voice.setMessageOrder(order);
        voice.setTranscript(question);
        voice.setSummary("AI 질문");
        voice.setRole("ai");
        voice.setCreatedAt(java.time.LocalDateTime.now());
        
        aiVoiceRepository.save(voice);
    }

    // 새 면접 시작 (첫 질문과 함께)
    public String startNewInterview(String userId) {
        String conversationId = createNewConversation(userId);
        
        // 첫 번째 AI 질문 저장
        saveAiQuestion(userId, conversationId, INTERVIEW_QUESTIONS[0], 1);
        
        return conversationId;
    }

    // 다음 질문 가져오기
    public String getNextQuestion(String conversationId) {
        int currentCount = aiVoiceRepository.countByConversationId(conversationId);
        int questionIndex = (currentCount - 1) / 2; // AI 질문과 사용자 답변이 쌍으로 저장되므로
        
        if (questionIndex < INTERVIEW_QUESTIONS.length) {
            return INTERVIEW_QUESTIONS[questionIndex];
        }
        return "면접이 완료되었습니다. 수고하셨습니다!";
    }

    // 대화방 제목 가져오기
    public String getConversationTitle(String conversationId) {
        AiVoice firstQuestion = aiVoiceRepository.findFirstByConversationIdAndRoleOrderByMessageOrderAsc(conversationId, "ai");
        if (firstQuestion != null) {
            return "음성면접 - " + firstQuestion.getTranscript().substring(0, Math.min(20, firstQuestion.getTranscript().length())) + "...";
        }
        return "음성면접";
    }
} 
