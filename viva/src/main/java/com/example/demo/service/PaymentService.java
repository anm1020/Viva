package com.example.demo.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Payment;
import com.example.demo.model.entity.Payment.PayStatus;
import com.example.demo.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository repo;

	/**
	 * 결제 준비(레코드 생성)
	 */
	@Transactional
	public Payment preparePayment(Long resNo, BigDecimal amount) {
		Payment p = Payment.builder().payResno(resNo.toString()).payAmount(amount).payStatus(PayStatus.ready)
				.payCreateDt(new Timestamp(System.currentTimeMillis())).build();
		return repo.save(p);
	}

	/**
	 * 결제 완료 콜백 처리 (상태 업데이트)
	 */
	@Transactional
	public void confirmPayment(Long payId) {
		Payment p = repo.findById(payId).orElseThrow(() -> new IllegalArgumentException("잘못된 payId"));
		p.setPayStatus(PayStatus.paid);
		repo.save(p);
	}

	//결제저장
	public Payment savePayment(Payment payment) {
		 // 결제 수단이 명시되지 않았으면 기본값으로 CARD 설정
		if (payment.getPayType() == null) {
	        payment.setPayType(Payment.PayType.CARD);  // 또는 POINT, 상황에 따라
	    }
		return repo.save(payment);

	}

	public int getPayAmountByResId(Long resId) {
		List<Payment> payments = repo.findByResId(resId);
		 return payments.stream()
			        .filter(p -> p.getPayStatus() == Payment.PayStatus.paid) // 결제 완료된 것만 걸러냄
			        .findFirst() // 가장 먼저 찾은 결제건만 사용
			        .map(p -> p.getPayAmount().intValue()) // BigDecimal을 int로 변환
			        .orElse(0); // 없으면 0원 반환
			}
	

	/**
	 * 주어진 예약 ID가 결제 완료 상태인지 확인합니다.
	 */
	public boolean isPaidReservation(Long resId) {
		List<Payment> payments = repo.findByResId(resId);

	    // 카드 or 포인트 둘 중 하나라도 결제된 상태라면 true
		// 상태가 "paid" 인 결제가 하나라도 있으면 true
		return payments.stream().anyMatch(p -> p.getPayStatus() == Payment.PayStatus.paid);
	}
	
	// 환불 상태로 변경 resId로 해당 예약의 결제 내역 중 상태가 paid인 걸 찾고 그걸 refunded로 바꿔줌
	@Transactional
	public void markAsRefunded(Long resId) {
	    List<Payment> payments = repo.findByResId(resId);
	    for (Payment p : payments) {
	        if (p.getPayStatus() == Payment.PayStatus.paid) {
	            p.setPayStatus(Payment.PayStatus.refunded);
	            repo.save(p);
	            break; // 한 건만 처리
	        }
	    }
	}

	
public Payment findLatestPaidByResId(Long resId) {
    return repo.findByResIdOrderByPayCreateDtDesc(resId).stream()
        .filter(p -> p.getPayStatus() == Payment.PayStatus.paid)
        .findFirst()
        .orElse(null); // 없으면 null 반환
}

}
