package com.example.demo.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "point_exchange")
public class PointExchange {

	 // 환전 신청 고유 ID (자동 생성, PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신청 회원 ID (users 테이블의 user_id 참조, varchar(50))
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    // 환전자 이름
    @Column(nullable = false, length = 100)
    private String name;

    // 휴대폰 번호
    @Column(nullable = false, length = 20)
    private String phone;

    // 은행명
    @Column(nullable = false, length = 50)
    private String bank;

    // 계좌번호
    @Column(nullable = false, length = 50)
    private String account;

    // 환전 신청 포인트 금액
    @Column(nullable = false)
    private int amount;

    // 환전 신청 시간
    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    // 처리 상태 (예: PENDING, APPROVED, REJECTED), 기본값 PENDING
    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    // 기본 생성자 (JPA 필수)
    public PointExchange() {}
    // 생성자, getter, setter, toString 등 필요시 작성
}
