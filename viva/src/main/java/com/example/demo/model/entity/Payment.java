package com.example.demo.model.entity;

import java.math.BigDecimal;
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
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결제 트랜잭션 정보를 저장하는 엔티티
 */
@Data
@Entity
@Table(name = "payment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "pay_id")
	    private Long payId;                      // 결제 PK

	    @Column(name = "pay_amount", nullable = false, precision = 12, scale = 2)
	    private BigDecimal payAmount;            // 결제 금액

	    @Enumerated(EnumType.STRING)
	    @Column(name = "pay_status", nullable = false)
	    private PayStatus payStatus;             // 결제 상태

	    @Column(name = "pay_resno", nullable = false)
	    private String payResno;                 // 예약 번호 (FK)

	    @Column(name = "pay_create_dt", nullable = false, updatable = false)
	    private LocalDateTime payCreateDt;       // 결제 시각

	    public enum PayStatus {
	        ready,      // 준비 중
	        paid,       // 결제 완료
	        failed,     // 결제 실패
	        cancelled,  // 결제 취소
	        refunded    // 환불 완료
	    }
	}
