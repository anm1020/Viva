package com.example.demo.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "interviewer")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intr_id")
    private Long intrId;

    @Column(name = "user_id")
    private String userId; // Users 테이블과 직접 연관관계 맺지 않음

    @Column(name = "intr_intro")
    private String intrIntro;

    @Column(name = "intr_skills")
    private String intrSkills;

    @Column(name = "intr_image")
    private String intrImage;

    @Column(name = "intr_price")
    private Integer intrPrice;

    @Column(name = "intr_created_at")
    private LocalDateTime intrCreatedAt;

    @Column(name = "intr_updated_at")
    private LocalDateTime intrUpdatedAt;
    
    @Column(name = "intr_cate", length = 255)
    private String intrCate; // 예: "WEB,FRONT,BACK"

    @Column(name = "intr_content", columnDefinition = "TEXT")
    private String intrContent;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private Users user;

    @PrePersist
    public void onCreate() {
        this.intrCreatedAt = LocalDateTime.now();
        this.intrUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.intrUpdatedAt = LocalDateTime.now();
    }

}

