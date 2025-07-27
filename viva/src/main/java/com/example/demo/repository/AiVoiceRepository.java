package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.AiVoice;

public interface AiVoiceRepository extends JpaRepository<AiVoice, Long> {

    // 로그인한 사용자 ID 기준으로 기록 조회
    List<AiVoice> findByUserIdOrderByCreatedAtDesc(String userId);

    // 특정 세션 ID 기준으로 조회 (옵션)
    List<AiVoice> findBySessionId(String sessionId);

    // 대화방 관련 메서드들
    List<AiVoice> findByConversationIdOrderByMessageOrderAsc(String conversationId);
    
    List<AiVoice> findByUserIdAndConversationIdIsNotNullOrderByCreatedAtDesc(String userId);
    
    List<String> findDistinctConversationIdByUserId(String userId);
    
    Integer countByConversationId(String conversationId);
    
    // 대화방 제목 조회 (첫 번째 AI 질문)
    AiVoice findFirstByConversationIdAndRoleOrderByMessageOrderAsc(String conversationId, String role);
}
