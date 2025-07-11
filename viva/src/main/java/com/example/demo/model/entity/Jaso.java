package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jaso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jaso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jaso_id")
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "jaso_title", nullable = false, length = 200)
    private String title;

    @Column(name = "jaso_growth", columnDefinition = "TEXT")
    private String growth;

    @Column(name = "jaso_personality", columnDefinition = "TEXT")
    private String personality;

    @Column(name = "jaso_school", columnDefinition = "TEXT")
    private String school;

    @Column(name = "jaso_motivation", columnDefinition = "TEXT")
    private String motivation;

    @Column(name = "jaso_future", columnDefinition = "TEXT")
    private String future;

    @Column(name = "jaso_experience", columnDefinition = "TEXT")
    private String experience;

    @Column(name = "created_dt", nullable = false)
    private String createdDt;

    @Column(name = "updated_dt")
    private String updatedDt;
}
