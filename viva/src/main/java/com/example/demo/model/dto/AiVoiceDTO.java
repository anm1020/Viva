package com.example.demo.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class AiVoiceDTO {

    private String userId;
    private String sessionId;
    private String transcript;
    private String summary;
    private List<FeedbackItem> details;
    private String speechSpeed;
    private String pronunciation;
    private String strengths;
    private String weaknesses;
    private String exampleSentence;
    private List<String> suggestions;
    private String conversationId;
    private Integer messageOrder;
    private String role;

    @Data
    public static class FeedbackItem {
        private String title;
        private String comment;
    }
}
