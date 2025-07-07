package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 필요하면 결제 내역 조회용 커스텀 메서드 추가
}
