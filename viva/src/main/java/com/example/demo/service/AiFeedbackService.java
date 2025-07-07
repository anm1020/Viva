package com.example.demo.service;

import com.example.demo.model.entity.AiFeedback;

import java.util.Optional;

public interface AiFeedbackService {
    void saveFeedback(AiFeedback feedback);
    Optional<AiFeedback> getFeedbackBySessionId(String sessionId);
}
