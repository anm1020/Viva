package com.example.demo.service;

import com.example.demo.model.entity.JasoFeedback;

import java.util.List;

public interface JasoFeedbackService {

    JasoFeedback saveFeedback(JasoFeedback feedback); // 피드백 저장
    List<JasoFeedback> getFeedbacksByJasoId(Long jasoId); // 해당 자소서에 대한 피드백 목록 조회
    void deleteFeedback(Long id); // 피드백 삭제
    List<JasoFeedback> getFeedbackByJasoId(Long jasoId);
    JasoFeedback generateAndSaveFeedback(Long jasoId);
}
