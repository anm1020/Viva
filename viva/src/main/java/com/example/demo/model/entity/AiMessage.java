package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String messageId;  // VARCHAR(20)에 맞게 String 유지

    @Column(nullable = false)
    private String sessionId;  // FK 아님 (String)

    @Column(nullable = false)
    private String role; // user, ai, system 등

    @Column(columnDefinition = "TEXT")
    private String content;  // LONGTEXT에서 TEXT로 변경

    private String createdDt;
}
