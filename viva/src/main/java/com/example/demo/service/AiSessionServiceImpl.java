package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.AiSession;
import com.example.demo.repository.AiSessionRepository;
import com.example.demo.service.AiSessionService;

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
    public List<AiSession> findByMemberId(String memberId) {
        return aiSessionRepository.findAll()
                .stream()
                .filter(s -> s.getMemberId().equals(memberId))
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
}
