package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String messageId;

    @Column(nullable = false)
    private String sessionId;  // FK 아님 (String)

    @Column(nullable = false)
    private String role; // user, ai, system 등

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String createdDt;
}
