package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.model.entity.AiMessage;

public interface AiMessageService {
	
	 AiMessage saveMessage(AiMessage message); 
	 
    List<AiMessage> getMessagesBySessionId(String sessionId);
    
    Map<String, AiMessage> sendAndReply(String sessionId, String userPrompt);
    
    
}
