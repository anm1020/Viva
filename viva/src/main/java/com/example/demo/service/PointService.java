package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Reservation;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.TestUserRepository;
import com.example.demo.repository.UserPointRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

	private final UserPointRepository userPointRepo;

	private final TestUserRepository userRepository;
	private final ReservationRepository resRepository;

	// 포인트 충전
	@Transactional
	public void charge(String userId, int amount) {
		userPointRepo.findByUserId(userId).ifPresentOrElse(point -> {
			point.setPoint(point.getPoint() + amount);
			userPointRepo.save(point);
		}, () -> {
			var newPoint = new com.example.demo.model.entity.UserPoint();
			newPoint.setUserId(userId);
			newPoint.setPoint(amount);
			userPointRepo.save(newPoint);
		});
	}

	// 포인트 조회
	public int getPoint(String userId) {
		return userPointRepo.findByUserId(userId).map(p -> p.getPoint()).orElse(0); // 없으면 0원
	}

	// 포인트 결제
	public boolean use(String userId, int amount) {
		return userPointRepo.findByUserId(userId).map(point -> {
			if (point.getPoint() >= amount) {
				point.setPoint(point.getPoint() - amount);
				userPointRepo.save(point);
				return true;
			}
			return false; // 포인트 부족
		}).orElse(false); // 포인트 데이터 없음
	}

	//상태를 'confirmed'로 변경
	@Transactional
	public void markReservationAsPaid(Long resId) {
		resRepository.findById(resId).ifPresent(reservation -> {
			reservation.setResStatus("confirmed");
			resRepository.save(reservation); // ← 수정된 부분
		});
	}
	//상태를 "cancelled" 변경
	@Transactional
	public void cancelReservation(Long resId) {
	    resRepository.findById(resId).ifPresent(res -> {
	        res.setResStatus("cancelled");
	        resRepository.save(res);
	    });
	}

	// 포인트 환불 (내부적으로 charge 메서드 재사용)
	public void refundPoint(String userId, int amount) {
	    // 환불도 결국 사용자에게 포인트를 돌려주는 것이므로 charge 재사용
	    charge(userId, amount);
	}
	
	
}