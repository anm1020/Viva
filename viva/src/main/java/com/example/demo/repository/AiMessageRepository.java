package com.example.demo.repository;

import com.example.demo.model.entity.AiMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AiMessageRepository extends JpaRepository<AiMessage, String> {
    
    @Query("SELECT m FROM AiMessage m WHERE m.sessionId = :sessionId ORDER BY m.createdDt ASC")
    List<AiMessage> findBySessionIdOrderByCreatedDtAsc(@Param("sessionId") String sessionId);
}
