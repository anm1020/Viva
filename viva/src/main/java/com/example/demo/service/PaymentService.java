package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
     * 3) 결제 준비(레코드 생성)
     */
    @Transactional
    public Payment preparePayment(Long resNo, BigDecimal amount) {
        Payment p = Payment.builder()
            .payResno(resNo.toString())
            .payAmount(amount)
            .payStatus(PayStatus.ready)
            .payCreateDt(LocalDateTime.now())
            .build();
        return repo.save(p);
    }

    /**
     * 4) 결제 완료 콜백 처리 (상태 업데이트)
     */
    @Transactional
    public void confirmPayment(Long payId) {
        Payment p = repo.findById(payId)
            .orElseThrow(() -> new IllegalArgumentException("잘못된 payId"));
        p.setPayStatus(PayStatus.paid);
        repo.save(p);
    }

	public Payment savePayment(Payment payment) {
		return repo.save(payment);
		
	}
}
