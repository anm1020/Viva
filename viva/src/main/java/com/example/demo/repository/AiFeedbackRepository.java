package com.example.demo.repository;

import com.example.demo.model.entity.AiFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiFeedbackRepository extends JpaRepository<AiFeedback, String> {
    Optional<AiFeedback> findBySessionId(String sessionId);
}
