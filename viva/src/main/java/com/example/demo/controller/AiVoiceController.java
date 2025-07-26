package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.AiVoiceDTO;
import com.example.demo.model.entity.AiVoice;
import com.example.demo.repository.AiVoiceRepository;
import com.example.demo.service.AiVoiceService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AiVoiceController {

    private final AiVoiceService aiVoiceService;
    private final AiVoiceRepository aiVoiceRepository;
    

    // 저장 (API 호출 시 사용)
    @PostMapping("/ai/voice/save")
    @ResponseBody
    public String saveVoice(@RequestBody AiVoiceDTO dto, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "로그인 필요";

        aiVoiceService.saveVoiceFeedback(userId, dto);
        return "ok";
    }

    // 피드백 저장 (새로운 엔드포인트)
    @PostMapping("/ai/voice/save-feedback")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveFeedback(@RequestBody Map<String, Object> feedbackData, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인 필요"));
        }

        try {
            AiVoiceDTO dto = new AiVoiceDTO();
            dto.setUserId(userId);
            dto.setSessionId((String) feedbackData.get("sessionId"));
            dto.setTranscript((String) feedbackData.get("transcript"));
            dto.setSummary((String) feedbackData.get("summary"));
            dto.setSpeechSpeed((String) feedbackData.get("speechSpeed"));
            dto.setPronunciation((String) feedbackData.get("pronunciation"));
            dto.setStrengths((String) feedbackData.get("strengths"));
            dto.setWeaknesses((String) feedbackData.get("weaknesses"));
            dto.setExampleSentence((String) feedbackData.get("example"));
            
            // details와 suggestions는 JSON으로 변환
            if (feedbackData.get("details") != null) {
                dto.setDetails((List<AiVoiceDTO.FeedbackItem>) feedbackData.get("details"));
            }
            if (feedbackData.get("suggestions") != null) {
                dto.setSuggestions((List<String>) feedbackData.get("suggestions"));
            }

            aiVoiceService.saveVoiceFeedback(userId, dto);
            return ResponseEntity.ok(Map.of("message", "피드백이 성공적으로 저장되었습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "저장 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // 저장된 피드백 목록 조회 (JSON)
    @GetMapping("/ai/voice/feedback-list")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getFeedbackList(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(List.of());
        }

        try {
            List<AiVoice> feedbacks = aiVoiceService.getVoiceFeedbackList(userId);
            List<Map<String, Object>> result = feedbacks.stream()
                .map(feedback -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", feedback.getId());
                    map.put("sessionId", feedback.getSessionId());
                    map.put("summary", feedback.getSummary());
                    map.put("createdAt", feedback.getCreatedAt().toString());
                    return map;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of());
        }
    }

    // 저장된 피드백 단건 상세 조회 (JSON)
    @GetMapping("/ai/voice/feedback-detail/{id}")
    @ResponseBody
    public ResponseEntity<?> getVoiceFeedbackDetail(@PathVariable("id") Long id, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "로그인 필요"));
        AiVoice feedback = aiVoiceRepository.findById(id).orElse(null);
        if (feedback == null || !feedback.getUserId().equals(userId)) {
            return ResponseEntity.status(404).body(Map.of("error", "피드백 없음"));
        }
        return ResponseEntity.ok(feedback);
    }

    // 피드백 삭제 API
    @DeleteMapping("/ai/voice/feedback/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteVoiceFeedback(@PathVariable("id") Long id, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "로그인 필요"));
        AiVoice feedback = aiVoiceRepository.findById(id).orElse(null);
        if (feedback == null || !feedback.getUserId().equals(userId)) {
            return ResponseEntity.status(404).body(Map.of("error", "피드백 없음"));
        }
        aiVoiceRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "삭제 완료"));
    }

    // 음성 파일 텍스트 변환
    @PostMapping("/ai/voice/transcribe")
    @ResponseBody
    public ResponseEntity<Map<String, String>> transcribeVoice(@RequestParam("file") MultipartFile file) {
        try {
            String text = aiVoiceService.transcribe(file);
            return ResponseEntity.ok(Map.of("text", text));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "음성 변환 실패: " + e.getMessage()));
        }
    }

    // 히스토리 목록 조회
    @GetMapping("/ai/voice/history")
    public String getVoiceHistory(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) return "redirect:/login";

        List<AiVoice> feedbackList = aiVoiceService.getVoiceFeedbackList(userId);
        model.addAttribute("feedbackList", feedbackList);
        return "ai/voice-history";
    }
    @PostMapping("/ai/voice/interview")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processVoiceInterview(@RequestParam("file") MultipartFile file,
                                                                     @RequestParam("sessionId") String sessionId) {
        try {
            // Whisper API로 텍스트 추출
            String transcript = aiVoiceService.transcribe(file);

            // GPT API로 상세 피드백 생성
            String gptPrompt = String.format("""
                다음 면접 답변을 분석하여 상세한 피드백을 제공해주세요.
                
                답변 내용: %s
                
                다음 형식으로 JSON 응답을 제공해주세요:
                {
                    "summary": "전체 답변 요약 (2-3문장)",
                    "speechSpeed": "말속도 평가",
                    "pronunciation": "발음 평가", 
                    "strengths": "장점 분석",
                    "weaknesses": "개선점 분석",
                    "details": [
                        {"title": "내용 구성", "comment": "구체적인 피드백"},
                        {"title": "표현력", "comment": "구체적인 피드백"},
                        {"title": "자신감", "comment": "구체적인 피드백"}
                    ],
                    "example": "개선된 답변 예시 문장",
                    "suggestions": [
                        "다음 단계 제안 1",
                        "다음 단계 제안 2", 
                        "다음 단계 제안 3"
                    ]
                }
                """, transcript);

            String gptResponse = aiVoiceService.generateVoiceFeedback(gptPrompt);
            
            // JSON 파싱
            Map<String, Object> feedbackData = aiVoiceService.parseFeedbackResponse(gptResponse);
            
            // 결과 Map 구성
            Map<String, Object> result = new HashMap<>();
            result.put("transcript", transcript);
            result.put("summary", feedbackData.getOrDefault("summary", "분석 중..."));
            result.put("speechSpeed", feedbackData.getOrDefault("speechSpeed", "분석 중..."));
            result.put("pronunciation", feedbackData.getOrDefault("pronunciation", "분석 중..."));
            result.put("strengths", feedbackData.getOrDefault("strengths", "분석 중..."));
            result.put("weaknesses", feedbackData.getOrDefault("weaknesses", "분석 중..."));
            result.put("details", feedbackData.getOrDefault("details", List.of()));
            result.put("example", feedbackData.getOrDefault("example", ""));
            result.put("suggestions", feedbackData.getOrDefault("suggestions", List.of()));
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "음성 분석 실패: " + e.getMessage()));
        }
    }

    // 대화방 목록 페이지
    @GetMapping("/ai/voice/conversations")
    public String getConversationList(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) return "redirect:/login";

        List<String> conversationIds = aiVoiceService.getUserConversationIds(userId);
        model.addAttribute("conversationIds", conversationIds);
        return "Ai/voice-conversations";
    }

    // 특정 대화방 페이지
    @GetMapping("/ai/voice/conversation/{conversationId}")
    public String getConversation(@PathVariable String conversationId, 
                                 HttpSession session, Model model) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) return "redirect:/login";

        List<AiVoice> messages = aiVoiceService.getConversationMessages(conversationId);
        model.addAttribute("conversationId", conversationId);
        model.addAttribute("messages", messages);
        return "Ai/voice-conversation";
    }

    // 새 대화방 생성
    @PostMapping("/ai/voice/conversation/new")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createNewConversation(HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인 필요"));
        }

        String conversationId = aiVoiceService.createNewConversation(userId);
        return ResponseEntity.ok(Map.of("conversationId", conversationId));
    }

    // 대화방에 메시지 저장
    @PostMapping("/ai/voice/conversation/save")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveConversationMessage(
            @RequestParam("conversationId") String conversationId,
            @RequestParam("transcript") String transcript,
            @RequestParam("feedback") String feedback,
            HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인 필요"));
        }

        aiVoiceService.saveVoiceMessage(userId, conversationId, transcript, feedback);
        return ResponseEntity.ok(Map.of("message", "저장 완료"));
    }

    // 실시간 음성면접 페이지
    @GetMapping("/ai/voice/chat")
    public String voiceChatPage(@RequestParam(value = "conversationId", required = false) String conversationId,
                               HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        // 새 면접 시작
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = aiVoiceService.startNewInterview(userId);
        }

        // 대화방 목록
        List<String> conversationIds = aiVoiceService.getUserConversationIds(userId);
        model.addAttribute("conversationIds", conversationIds);
        
        // 현재 대화방 정보
        model.addAttribute("currentConversationId", conversationId);
        model.addAttribute("conversationTitle", aiVoiceService.getConversationTitle(conversationId));
        
        // 현재 대화방의 메시지들
        List<AiVoice> messages = aiVoiceService.getConversationMessages(conversationId);
        model.addAttribute("messages", messages);

        return "Ai/voice-chat";
    }

    // 다음 질문 가져오기
    @GetMapping("/ai/voice/next-question")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getNextQuestion(@RequestParam("conversationId") String conversationId) {
        String nextQuestion = aiVoiceService.getNextQuestion(conversationId);
        return ResponseEntity.ok(Map.of("question", nextQuestion));
    }

    // 실시간 음성 답변 저장
    @PostMapping("/ai/voice/chat/save")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveChatMessage(@RequestParam("conversationId") String conversationId,
                                                              @RequestParam("transcript") String transcript,
                                                              HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인 필요"));
        }

        // AI 피드백 생성
        String feedback = "음성 인식 결과: " + transcript + "\n\n" +
                         "면접 답변으로서 적절한 내용입니다. 자신감 있게 답변하시고, 구체적인 경험을 포함하면 더 좋겠습니다.";

        // 사용자 답변 저장
        aiVoiceService.saveVoiceMessage(userId, conversationId, transcript, feedback);

        // 다음 질문 가져오기
        String nextQuestion = aiVoiceService.getNextQuestion(conversationId);
        
        // 다음 AI 질문 저장
        int nextOrder = aiVoiceRepository.countByConversationId(conversationId) + 1;
        aiVoiceService.saveAiQuestion(userId, conversationId, nextQuestion, nextOrder);

        return ResponseEntity.ok(Map.of(
            "message", "저장 완료",
            "nextQuestion", nextQuestion
        ));
    }

    @PostMapping("/ai/voice/session/new")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createNewVoiceSession(HttpSession session, @RequestParam(value = "title", defaultValue = "음성면접") String title) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인 필요"));
        }
        String sessionId = aiVoiceService.createNewVoiceSession(userId, title);
        return ResponseEntity.ok(Map.of("sessionId", sessionId));
    }

}
