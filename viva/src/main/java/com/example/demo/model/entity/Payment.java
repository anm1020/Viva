package com.example.demo.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "payments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pay_id")
	private Long payId; // 결제 PK

	@Column(name = "pay_amount", precision = 12, scale = 2)
	private BigDecimal payAmount; // 결제 금액

	@Enumerated(EnumType.STRING)
	@Column(name = "pay_status")
	private PayStatus payStatus; // 결제 상태

	@Column(name = "pay_resno")
	private String payResno; // 예약 번호 

	@Column(name = "res_id")
	private Long resId; // 예약 ID (FK)

	@Column(name = "pay_create_dt", updatable = false)
	@CreationTimestamp
	private Timestamp payCreateDt; // 결제 시각
	
	@Column(name = "user_id")
	private String userId; // 회원번호 (FK)
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@Enumerated(EnumType.STRING) // 반드시 추가!
	@Column(name = "pay_type", nullable = false)
	private PayType payType;	// 결제수단
	
	public enum PayType {
	    CARD,   // 카드 결제
	    POINT,   // 포인트 결제
	    CHARGE   // 포인트 충전 내역
	}

	public enum PayStatus {
		ready, // 준비 중
		paid, // 결제 완료
		cancelled, // 결제 취소
		refunded // 환불 완료
	}
}
