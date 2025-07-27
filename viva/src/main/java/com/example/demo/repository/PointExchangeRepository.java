package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.PointExchange;

public interface PointExchangeRepository extends JpaRepository<PointExchange, Long> {

	// 환전내역 조회
	   List<PointExchange> findByUserIdOrderByRequestedAtDesc(String userId);
	   
	// (예원추가) 관리자용: PENDING 상태 요청만 (최근 순)
	    List<PointExchange> findByStatusOrderByRequestedAtDesc(String status);
	    
}