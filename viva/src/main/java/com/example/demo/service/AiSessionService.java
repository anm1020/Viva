package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.entity.AiSession;

public interface AiSessionService {
    AiSession save(AiSession session);
    Optional<AiSession> findById(String sessionId);
    List<AiSession> findByUserId(String userId);
    List<AiSession> getAllSessions();
    AiSession saveSession(AiSession session);	
   List<AiSession>getSessionsByUserId(String userId);
   void updateSummary(String sessionId, String summary); 
   void deleteSessionById(String sessionId);
   void updateSessionTitle(String sessionId, String title);
}

