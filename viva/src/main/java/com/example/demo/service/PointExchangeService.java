package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.PointExchange;
import com.example.demo.repository.PointExchangeRepository;
import com.example.demo.repository.UserPointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointExchangeService {

    private final PointExchangeRepository pointExchangeRepository;
    private final UserPointRepository userPointRepository;

    /**
     * 환전 신청 처리
     * @param pointExchange 환전 신청 정보 (userId, amount 등 포함)
     */
    @Transactional
    public void requestExchange(PointExchange pointExchange) {
        String userId = pointExchange.getUserId();
        int amount = pointExchange.getAmount();

        // 1. 사용자 현재 포인트 조회
        int currentPoint = userPointRepository.findById(userId)
            .map(up -> up.getPoint())
            .orElseThrow(() -> new IllegalArgumentException("사용자 포인트 정보가 없습니다."));

        // 2. 잔액 부족 체크
        if (amount > currentPoint) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }

        // 3. 신청 시간 설정
        pointExchange.setRequestedAt(LocalDateTime.now());

        // 4. 상태 기본값 설정
        if (pointExchange.getStatus() == null) {
            pointExchange.setStatus("PENDING");
        }

        // 5. 환전 신청 저장
        pointExchangeRepository.save(pointExchange);

        // 6. 포인트 차감
        int updated = userPointRepository.deductPoint(userId, amount);
        if (updated == 0) {
            throw new IllegalArgumentException("포인트 차감에 실패했습니다.");
        }
    }
    //조회
    public List<PointExchange> getUserExchangeList(String userId) {
    	 List<PointExchange> list = pointExchangeRepository.findByUserIdOrderByRequestedAtDesc(userId);
    	    System.out.println("환전 내역 개수: " + (list == null ? 0 : list.size()));
    return pointExchangeRepository.findByUserIdOrderByRequestedAtDesc(userId);
}
}
