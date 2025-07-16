package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.model.dto.ChatMessageDTO;
import com.example.demo.model.entity.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;

	public void saveMessage(ChatMessageDTO dto) {
	    ChatMessage entity = ChatMessage.builder()
	        .roomId(Integer.parseInt(dto.getRoomId()))
	        .sender(dto.getSender())
	        .message(dto.getMessage())
	        .timestamp(LocalDateTime.now()) // or parse if timestamp가 포함된다면
	        .build();

	    chatMessageRepository.save(entity);
	}
}
