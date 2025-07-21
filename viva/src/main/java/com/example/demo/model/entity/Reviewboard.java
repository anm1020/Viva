package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity  
@Data  
@Table(name = "reviewboard")  
public class Reviewboard {

    @Id  // PK 필드 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동증가 전략 사용
    private Long reviewId;  // 리뷰 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 관계, 지연로딩
    @JoinColumn(name = "user_id")  // 외래키 컬럼명 지정 (users 테이블의 PK)
    private Users user;  // 리뷰 작성자 정보 (Users 엔티티와 연관관계)

    @Column(nullable = false, length = 200)  // NOT NULL, 최대 길이 200
    private String title;  // 리뷰 제목

    @Column(nullable = false, columnDefinition = "TEXT")  // NOT NULL, TEXT 타입
    private String content;  // 리뷰 내용

    @Column(nullable = false)  // NOT NULL
    private Integer rating;  // 별점 (1~5)

    @Column(nullable = false)
    private Integer viewCount = 0;  // 조회수, 기본값 0

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 생성 일시

    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정 일시

    // 엔티티 저장 직전에 자동으로 실행되는 메서드
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;  // 생성시간 설정
        updatedAt = now;  // 수정시간도 현재 시간으로 설정
        if (viewCount == null) viewCount = 0;  // 조회수가 null일 경우 0으로 초기화
    }

    // 엔티티 수정 직전에 자동으로 실행되는 메서드
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();  // 수정시간 갱신
    }

    public Reviewboard() {}  // 기본 생성자

}
