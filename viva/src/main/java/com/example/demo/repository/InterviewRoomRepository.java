package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.InterviewRoom;
import com.example.demo.model.entity.Reservation;
import com.example.demo.model.entity.Users;

@Repository
public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, Integer> {

	// 기본 CRUD는 JpaRepository에서 제공됨

	// 내림차순 검색
	List<InterviewRoom> findAllByOrderByIntrRoomIdDesc();
	
	// 예시: 상태별로 조회
	List<InterviewRoom> findByStatusCd(String statusCd);

	// 예시: 특정 사용자가 만든 방 목록 조회
	List<InterviewRoom> findByHost(Users host);

	// 예약 번호로 된 방이 생성된지 체크
	boolean existsByReservation(Reservation reservation);
	
}
