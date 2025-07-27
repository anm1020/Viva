package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.AiSession;

public interface AiSessionRepository extends JpaRepository<AiSession, String> {
	// AiSessionRepository.java
	List<AiSession> findByUserIdOrderByCreatedDtDesc(String userId);


    
}
