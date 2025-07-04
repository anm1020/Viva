package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.entity.AiSession;

public interface AiSessionService {
    AiSession save(AiSession session);
    Optional<AiSession> findById(String sessionId);
    List<AiSession> findByMemberId(String memberId);
    List<AiSession> getAllSessions();
    AiSession saveSession(AiSession session);

}
