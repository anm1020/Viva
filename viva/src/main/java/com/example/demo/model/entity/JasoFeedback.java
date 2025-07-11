package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jaso_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JasoFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id;

    @Column(name = "jaso_id", nullable = false)
    private Long jasoId;  // FK

    @Column(name = "feedback_text", nullable = false, columnDefinition = "TEXT")
    private String feedbackContent;

    @Column(name = "created_dt", nullable = true)
    private String createdDt;
}
