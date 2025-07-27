package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.dto.AiRequestDTO;
import com.example.demo.model.entity.AiFeedback;
import com.example.demo.model.entity.AiMessage;
import com.example.demo.model.entity.AiSession;
import com.example.demo.service.AIService;
import com.example.demo.service.AiFeedbackService;
import com.example.demo.service.AiMessageService;
import com.example.demo.service.AiSessionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {

    private final AIService AIService;
    private final AiSessionService aiSessionService;
    private final AiFeedbackService aiFeedbackService;
    private final AiMessageService aiMessageService;

//    @GetMapping("/")
//    public String home() {
//        return "redirect:/gptchat"; 
//    }
    @GetMapping("/aivoice")
    public String showAiVoicePage() {
        return "Ai/aivoice"; // → templates/Ai/aivoice.html
    }

    @GetMapping("/aichat")
    public String chatPage(
        @RequestParam(value = "sessionId", required = false) String sessionId,
        @RequestParam(value = "new", required = false) String isNew,
        HttpSession httpSession, Principal principal, Model model) {
        String userId = null;
        if (principal != null) {
            userId = principal.getName();
        }

        // "새로운 채팅" 버튼에서 new=1로 들어오면 무조건 새 세션 생성
        if ("1".equals(isNew) && userId != null) {
            sessionId = UUID.randomUUID().toString();
            AiSession newSession = AiSession.builder()
                .sessionId(sessionId)
                .userId(userId)
                .title("새로운 대화")
                .createdDt(java.time.LocalDateTime.now().toString())
                .build();
            aiSessionService.saveSession(newSession);
        } else if ((sessionId == null || sessionId.isEmpty()) && userId != null) {
            List<AiSession> userSessions = aiSessionService.getSessionsByUserId(userId);
            if (userSessions != null && !userSessions.isEmpty()) {
                sessionId = userSessions.get(0).getSessionId();
            } else {
                sessionId = UUID.randomUUID().toString();
                AiSession newSession = AiSession.builder()
                    .sessionId(sessionId)
                    .userId(userId)
                    .title("새로운 대화")
                    .createdDt(java.time.LocalDateTime.now().toString())
                    .build();
                aiSessionService.saveSession(newSession);
            }
        } else if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
        }

        List<AiMessage> messages = aiMessageService.getMessagesBySessionId(sessionId);
        model.addAttribute("messages", messages);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("sessionTitle", aiSessionService.findById(sessionId).map(AiSession::getTitle).orElse("새로운 대화"));

        // 로그인한 사용자의 세션 목록 가져오기
        if (userId != null) {
            List<AiSession> sessions = aiSessionService.getSessionsByUserId(userId);
            model.addAttribute("sessions", sessions != null ? sessions : List.of());
        } else {
            model.addAttribute("sessions", List.of());
        }

        return "Ai/aichat";
    }


    @PostMapping("/aichat")
    public String getChatResponse(@RequestParam("prompt") String prompt, Model model) {
        try {
            String response = AIService.askChatGPT(prompt);
            model.addAttribute("prompt", prompt);
            model.addAttribute("response", response);
        } catch (Exception e) {
            model.addAttribute("prompt", prompt);
            model.addAttribute("response", " 오류 발생: " + e.getMessage());
            e.printStackTrace(); // 콘솔에 전체 로그 남김
        }
        return "Ai/aichat";
    }
    // 세션 목록 보기
    @GetMapping("/sessions")
    public String listSessions(Model model) {
        model.addAttribute("sessions", aiSessionService.getAllSessions());
        return "Ai/session-list";  // templates/Ai/session-list.html
    }

    // 세션 생성 폼 페이지
    @GetMapping("/session/new")
    public String newSessionForm(Model model) {
        model.addAttribute("aiSession", new AiSession());
        return "Ai/session-form";  // templates/Ai/session-form.html
    }

    // 세션 저장 처리
    @PostMapping("/session/save")
    public String saveSession(@ModelAttribute AiSession aiSession) {
        aiSession.setSessionId(UUID.randomUUID().toString());
        aiSessionService.saveSession(aiSession);
        return "redirect:/ai/sessions";
    }

    // 피드백 폼 페이지
    @GetMapping("/feedback/new")
    public String newFeedbackForm(@RequestParam("sessionId") String sessionId, Model model) {
        AiFeedback feedback = new AiFeedback();
        feedback.setSessionId(sessionId);
        model.addAttribute("feedback", feedback);
        return "Ai/feedback-form"; 
    }

    // 피드백 저장 처리
    @PostMapping("/feedback/save")
    public String saveFeedback(@ModelAttribute AiFeedback feedback) {
        feedback.setFeedbackId(UUID.randomUUID().toString());
        aiFeedbackService.saveFeedback(feedback);
        return "redirect:/ai/sessions";
    }

    // 특정 세션의 피드백 조회
    @GetMapping("/feedback/view")
    public String viewFeedback(@RequestParam("sessionId") String sessionId, Model model) {
        AiFeedback feedback = aiFeedbackService.getFeedbackBySessionId(sessionId).orElse(null);
        model.addAttribute("feedback", feedback);
        return "Ai/feedback-view";  // templates/Ai/feedback-view.html
    }
    
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody AiMessage message) {
        AiMessage saved = aiMessageService.saveMessage(message); 
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getMessages(@PathVariable("sessionId") String sessionId) {
        return ResponseEntity.ok(aiMessageService.getMessagesBySessionId(sessionId));
    }
    
    @PostMapping("/sendAndReply")
    public ResponseEntity<Map<String, AiMessage>> sendAndReply(@RequestBody AiRequestDTO request) {
        try {
             // 1. 내 질문과 세션ID를 받아서
            Map<String, AiMessage> result = aiMessageService.sendAndReply(request.getSessionId(), request.getPrompt());
            // 2. AI 답변까지 생성해서 반환
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 기본 응답 생성
            AiMessage errorMessage = AiMessage.builder()
                    .sessionId(request.getSessionId())
                    .role("assistant")
                    .content("서버 오류가 발생했습니다: " + e.getMessage())
                    .createdDt(java.time.LocalDateTime.now().toString())
                    .build();
            
            // null 체크 후 Map 생성
            Map<String, AiMessage> errorResult = Map.of(
                    "userMessage", new AiMessage(),  // null 대신 빈 객체
                    "assistantMessage", errorMessage
            );
            return ResponseEntity.ok(errorResult);
        }
    }
    
    @GetMapping("/messages/{sessionId}")
    public ResponseEntity<List<AiMessage>> getMessagesBySessionId(@PathVariable("sessionId") String sessionId) {
        try {
            // sessionId 유효성 검사
            if (sessionId == null || sessionId.trim().isEmpty()) {
                System.out.println("SessionId가 비어있습니다.");
                return ResponseEntity.ok(List.of());
            }
            
            List<AiMessage> messages = aiMessageService.getMessagesBySessionId(sessionId);
            
            // null 체크
            if (messages == null) {
                System.out.println("SessionId: " + sessionId + "에 대한 메시지가 null입니다.");
                return ResponseEntity.ok(List.of());
            }
            
            System.out.println("SessionId: " + sessionId + ", Messages count: " + messages.size());
            System.out.println("Messages: " + messages);
            return ResponseEntity.ok(messages);
            
        } catch (Exception e) {
            System.err.println("메시지 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            // 500 오류 대신 빈 리스트 반환
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/my-sessions")
    public String mySessions(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("loginUserId"); // 로그인 시 세션에 저장된 값
        if (userId == null) {
            return "redirect:/login"; // 비로그인 상태
        }

        List<AiSession> sessions = aiSessionService.getSessionsByUserId(userId);
        model.addAttribute("sessions", sessions);
        return "Ai/my-session-list";  // templates/Ai/my-session-list.html
    }

    // 세션 삭제
    @DeleteMapping("/session/delete")
    public ResponseEntity<?> deleteSession(@RequestParam("sessionId") String sessionId) {
        aiSessionService.deleteSessionById(sessionId);
        aiMessageService.deleteMessagesBySessionId(sessionId);
        return ResponseEntity.ok().build();
    }

    // 세션 제목 업데이트
    @PostMapping("/session/updateTitle")
    public ResponseEntity<?> updateSessionTitle(@RequestBody Map<String, String> req) {
        String sessionId = req.get("sessionId");
        String title = req.get("title");
        aiSessionService.updateSessionTitle(sessionId, title);
        return ResponseEntity.ok().build();
    }

}
