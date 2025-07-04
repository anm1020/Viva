package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiFeedback {

    @Id
    private String feedbackId; // 예: UUID

    @Column(nullable = false)
    private String sessionId; // 대화방 ID

    private String speechSpeed;      // 말속도 평가
    private String pronunciation;    // 발음 평가

    @Lob
    private String summary;          // 전체 발표 내용 요약

    @Lob
    private String strengths;        // 장점

    @Lob
    private String weaknesses;       // 단점

    @Column(name = "created_dt")
    private String createdDt;        // 문자열 형태로 저장
}
