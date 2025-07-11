package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.AiSession;
import com.example.demo.repository.AiSessionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiSessionServiceImpl implements AiSessionService {

    private final AiSessionRepository aiSessionRepository;

    @Override
    public AiSession save(AiSession session) {
        return aiSessionRepository.save(session);
    }

    @Override
    public Optional<AiSession> findById(String sessionId) {
        return aiSessionRepository.findById(sessionId);
    }

    @Override
    public List<AiSession> findByUserId(String userId) {
        return aiSessionRepository.findAll()
                .stream()
                .filter(s -> s.getUserId().equals(userId))
                .toList();
    }
    
    @Override
    public List<AiSession> getAllSessions() {
        return aiSessionRepository.findAll();
    }
    
    @Override
    public AiSession saveSession(AiSession session) {
        return aiSessionRepository.save(session);
    }
    
    public List<AiSession> getSessionsByUserId(String userId) {
        return aiSessionRepository.findByUserIdOrderByCreatedDtDesc(userId);
    }
    
    @Override
    @Transactional
    public void updateSummary(String sessionId, String summary) {
        AiSession session = aiSessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
        session.setSummary(summary);  // JPA가 dirty checking으로 자동 업데이트
    }
    @Override
public void updateSessionTitle(String sessionId, String title) {
    AiSession session = aiSessionRepository.findById(sessionId).orElse(null);
    if (session != null) {
        session.setTitle(title);
        aiSessionRepository.save(session);
    }
}

@Override
public void deleteSessionById(String sessionId) {
    aiSessionRepository.deleteById(sessionId);
}
}
