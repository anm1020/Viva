package com.example.demo.repository;

import com.example.demo.model.entity.AiVoSession;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface AiVoSessionRepository extends JpaRepository<AiVoSession, String> {
    // 필요시 커스텀 메서드 추가
} 