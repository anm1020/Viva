package com.example.demo.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ai_voice")
@Getter
@Setter
public class AiVoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vo_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Lob
    @Column(name = "vo_transcript")
    private String transcript;

    @Lob
    @Column(name = "vo_summary")
    private String summary;

    @Lob
    @Column(name = "vo_details_json")
    private String detailsJson;

    @Column(name = "vo_speech_speed")
    private String speechSpeed;

    @Column(name = "vo_pronunciation")
    private String pronunciation;

    @Lob
    @Column(name = "vo_strengths")
    private String strengths;

    @Lob
    @Column(name = "vo_weaknesses")
    private String weaknesses;

    @Lob
    @Column(name = "vo_example_sentence")
    private String exampleSentence;

    @Lob
    @Column(name = "vo_suggestions_json")
    private String suggestionsJson;

    @Column(name = "vo_created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "vo_conversation_id")
    private String conversationId;

    @Column(name = "vo_message_order")
    private Integer messageOrder = 0;

    @Column(name = "vo_role")
    private String role = "user";
}
