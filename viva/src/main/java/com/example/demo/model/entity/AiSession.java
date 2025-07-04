package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiSession {

    @Id
    @Column(name = "session_id", length = 20)
    private String sessionId;

    @Column(name = "member_id", length = 20, nullable = false)
    private String memberId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "created_dt", length = 30)
    private String createdDt;
}
