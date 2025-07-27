package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.example.demo.repository.AiVoSessionRepository;
import com.example.demo.model.entity.AiVoSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@RequiredArgsConstructor
public class AiVoiceService {

    private final AiVoiceRepository aiVoiceRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    private AiVoSessionRepository aiVoSessionRepository;

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

    // 음성 면접 세션 생성
    public String createNewVoiceSession(String userId, String title) {
        String sessionId = "voice-" + java.util.UUID.randomUUID().toString().replace("-", "");
        AiVoSession session = AiVoSession.builder()
            .sessionId(sessionId)
            .userId(userId)
            .title(title)
            .sessionType("voice")
            .build();
        aiVoSessionRepository.save(session);
        return sessionId;
    }

    // GPT API를 호출하여 음성 피드백 생성
    public String generateVoiceFeedback(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "너는 면접 전문가입니다. 면접 답변을 분석하여 구체적이고 실용적인 피드백을 제공해주세요."));
            messages.add(Map.of("role", "user", "content", prompt));
            body.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", 
                request, 
                Map.class
            );

            if (response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    if (message != null) {
                        return (String) message.get("content");
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // GPT 응답 JSON 파싱
    public Map<String, Object> parseFeedbackResponse(String gptResponse) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // JSON 블록 추출
            int startIndex = gptResponse.indexOf("{");
            int endIndex = gptResponse.lastIndexOf("}") + 1;
            
            if (startIndex >= 0 && endIndex > startIndex) {
                String jsonStr = gptResponse.substring(startIndex, endIndex);
                return objectMapper.readValue(jsonStr, Map.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 파싱 실패 시 기본값 반환
        result.put("summary", "분석 중 오류가 발생했습니다.");
        result.put("speechSpeed", "분석 중...");
        result.put("pronunciation", "분석 중...");
        result.put("strengths", "분석 중...");
        result.put("weaknesses", "분석 중...");
        result.put("details", List.of());
        result.put("example", "");
        result.put("suggestions", List.of());
        
        return result;
    }
} 
