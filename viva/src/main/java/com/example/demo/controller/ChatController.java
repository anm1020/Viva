package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.dto.ChatMessageDTO;
import com.example.demo.service.ChatMessageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;
	private final ChatMessageService chatMessageService;
	
	@MessageMapping("/chat.send") 		// /app/chat.send 로 전송된 메시지 처리, 클라이언트 -> 서버 전송
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        message.setTimestamp(LocalDateTime.now().toString());
        
        // 입장, 퇴장시 알림은 DB에 저장하지 않음
        if (message.getMessage() != null && !message.getMessage().trim().isEmpty()
                && !message.getMessage().endsWith("님이 입장하셨습니다.")
                && !message.getMessage().endsWith("님이 퇴장하셨습니다.")) {
            // 메시지 저장
            chatMessageService.saveMessage(message);
        }
        
        // ✅ roomId를 이용한 동적 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomId(), message);
        
        return message;
    }
	
}
