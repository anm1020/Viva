package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_vo_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiVoSession {
    @Id
    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "created_dt")
    private LocalDateTime createdDt = LocalDateTime.now();

    @Column(name = "session_type", length = 20)
    private String sessionType = "voice";
} 