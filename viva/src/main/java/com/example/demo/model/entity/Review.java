package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter @Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_no")
    private Integer reviewNo;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "intr_id")
    private Integer intrId;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "content")
    private String content;

    @Column(name = "created_dt")
    private LocalDateTime createdDt;
} 