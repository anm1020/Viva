package com.example.demo.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_id")
    private Long resId;                      // 예약 PK

    @Column(name = "res_user_id", nullable = false)
    private String resUserId;                // 취준생 ID

    @Column(name = "res_intr_id", nullable = false)
    private String resIntrId;                // 면접관 ID

    @Column(name = "res_reserved_dt", nullable = false)
    private LocalDateTime resReservedDt;     // 예약된 일시

    @Enumerated(EnumType.STRING)
    @Column(name = "res_status", nullable = false)
    private ResStatus resStatus;             // 예약 상태

    @Column(name = "res_create_dt", nullable = false, updatable = false)
    private LocalDateTime resCreateDt;       // 생성 시각

    public enum ResStatus {
        pending,    // 요청 대기
        confirmed,  // 확정
        cancelled   // 취소
    }
}
