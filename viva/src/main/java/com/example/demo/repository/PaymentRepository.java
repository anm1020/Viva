package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 필요하면 결제 내역 조회용 커스텀 메서드 추가
	 // 로그인한 userId 로, payStatus = paid 인 결제 건의 resId 만 뽑기
    @Query("select p.resId from Payment p where p.userId = :userId and p.payStatus = com.example.demo.model.entity.Payment.PayStatus.paid")
    List<Long> findPaidResIdsByUserId(String userId);

    // 특정 예약(resId)으로 결제정보찾기
    List<Payment> findByResId(Long resId);
    
}
