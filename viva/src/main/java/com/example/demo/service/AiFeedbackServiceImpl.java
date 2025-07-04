package com.example.demo.service;

import com.example.demo.model.entity.AiFeedback;
import com.example.demo.repository.AiFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiFeedbackServiceImpl implements AiFeedbackService {

    private final AiFeedbackRepository aiFeedbackRepository;

    @Override
    public void saveFeedback(AiFeedback feedback) {
        aiFeedbackRepository.save(feedback);
    }

    @Override
    public Optional<AiFeedback> getFeedbackBySessionId(String sessionId) {
        return aiFeedbackRepository.findBySessionId(sessionId);
    }
}
