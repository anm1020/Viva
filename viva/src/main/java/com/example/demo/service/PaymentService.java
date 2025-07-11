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

	public Payment savePayment(Payment payment) {
		return repo.save(payment);

	}

	public int getPayAmountByResId(Long resId) {
		List<Payment> payments = repo.findByResId(resId);

		return payments.stream().filter(p -> p.getPayStatus() == Payment.PayStatus.paid).findFirst()
				.map(p -> p.getPayAmount().intValue()) // BigDecimal → int
				.orElse(0);
	}

	/**
	 * 주어진 예약 ID가 결제 완료 상태인지 확인합니다.
	 */
	public boolean isPaidReservation(Long resId) {
		List<Payment> payments = repo.findByResId(resId);
		

		// 상태가 "paid" 인 결제가 하나라도 있으면 true
		return payments.stream().anyMatch(p -> p.getPayStatus() == Payment.PayStatus.paid);
	}

}
