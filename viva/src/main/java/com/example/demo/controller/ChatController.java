package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.model.dto.ChatMessageDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/chat.send") 		// /app/chat.send 로 전송된 메시지 처리, 클라이언트 -> 서버 전송
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        message.setTimestamp(LocalDateTime.now().toString());
        // 👉 DB 저장 로직 추가 가능
        
        // ✅ roomId를 이용한 동적 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomId(), message);
        
        return message;
    }
	
}
