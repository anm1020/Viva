package com.example.demo.model.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "reservations")
@Data
//@NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "res_id")
	private Long resId; // PK: 예약 고유 번호

	@Column(name = "mem_id")
	private String memId; // FK: 예약자(회원). users.user_id 참조(role='mem')

	@Column(name = "intr_id")
	private String intrId; // FK: 면접관. users.user_id 참조 (role='intr')

	@Column(name = "reserved_date")
	private String reservedDate; // 예약된 일시 (면접 날짜/시간)

	@Column(name = "reserved_time")
	private String reservedTime; // 예약된 일시 (면접 날짜/시간)

	@Column(name = "res_status", nullable = false)
	private String resStatus; // 예약 상태 (pending, confirmed, cancelled)

	@CreationTimestamp
	@Column(name = "res_create_dt", updatable = false)
	private Timestamp resCreateDt; // 생성 시각 (등록된 시간, 자동 입력)
	
	@Transient
	private Payment payment;  // 결제 정보 (DB에 저장되지 않음)


}