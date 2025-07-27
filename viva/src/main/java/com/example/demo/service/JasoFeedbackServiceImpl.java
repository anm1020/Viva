package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Jaso;
import com.example.demo.model.entity.JasoFeedback;
import com.example.demo.repository.JasoFeedbackRepository;
import com.example.demo.repository.JasoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JasoFeedbackServiceImpl implements JasoFeedbackService {

    private final JasoFeedbackRepository jasoFeedbackRepository;
    private final JasoRepository jasoRepository;
    private final AIService aiService;

    @Override
    public JasoFeedback saveFeedback(JasoFeedback feedback) {
        return jasoFeedbackRepository.save(feedback);
    }

    @Override
    public List<JasoFeedback> getFeedbacksByJasoId(Long jasoId) {
        return jasoFeedbackRepository.findByJasoIdOrderByCreatedDtDesc(jasoId);
    }

    @Override
    public void deleteFeedback(Long id) {
        jasoFeedbackRepository.deleteById(id);
    }

    @Override
    public List<JasoFeedback> getFeedbackByJasoId(Long jasoId) {
        return jasoFeedbackRepository.findByJasoIdOrderByCreatedDtDesc(jasoId);
    }
     
 // AI 피드백 자동 생성 및 저장
    @Override
    public JasoFeedback generateAndSaveFeedback(Long jasoId) { 
        // 1. 자소서 조회
        Jaso jaso = jasoRepository.findById(jasoId) 
                .orElseThrow(() -> new RuntimeException("자소서를 찾을 수 없습니다."));

        // 2. GPT 프롬프트 생성 (각 필드 합침)
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("【성장과정】\n").append(jaso.getGrowth() != null ? jaso.getGrowth() : "").append("\n\n");
        promptBuilder.append("【성격의 장단점】\n").append(jaso.getPersonality() != null ? jaso.getPersonality() : "").append("\n\n");
        promptBuilder.append("【직무역량】\n").append(jaso.getSchool() != null ? jaso.getSchool() : "").append("\n\n");
        promptBuilder.append("【지원동기】\n").append(jaso.getMotivation() != null ? jaso.getMotivation() : "").append("\n\n");
        promptBuilder.append("【입사 후 포부】\n").append(jaso.getFuture() != null ? jaso.getFuture() : "").append("\n\n");
        promptBuilder.append("【기타 특별한 경험】\n").append(jaso.getExperience() != null ? jaso.getExperience() : "");
        String prompt = "다음 자기소개서를 읽고 개선이 필요한 부분이나 보완할 점을 피드백해주세요. 형식은 간단한 문장 2~3개로 작성해주세요:\n\n" + promptBuilder.toString();

        // 3. GPT 호출
        String feedback = aiService.askChatGPT(prompt);

        // 4. 저장
        return jasoFeedbackRepository.save(JasoFeedback.builder()
                .jasoId(jasoId)
                .feedbackContent(feedback)
                .createdDt(LocalDateTime.now().toString())
                .build());
    }

}
