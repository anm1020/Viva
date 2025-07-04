package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.ReservationDTO;
import com.example.demo.model.entity.Reservation;
import com.example.demo.model.entity.Reservation.ResStatus;
import com.example.demo.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

/**
 * Reservation(예약) 관련 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository repo;

	/**
	 * 1) 면접관의 해당 날짜에 가능한 시간대 목록 조회
	 */
	// 이걸로 사용테스트끝나면
//    @Transactional(readOnly = true)
//    public List<LocalTime> findAvailableSlots(String interviewerId, LocalDate date) {
//        // 예시: 09:00 ~ 17:00 매 정각
//        List<LocalTime> all = Stream.iterate(LocalTime.of(9, 0), t -> t.plusHours(1))
//                                    .limit(9)  // 9시간: 9~17시
//                                    .collect(Collectors.toList());
//        // 이미 예약된 시간 필터링
//        List<LocalTime> booked = repo.findAll().stream()
//            .filter(r -> r.getResIntrId().equals(interviewerId)
//                      && r.getResReservedDt().toLocalDate().equals(date))
//            .map(r -> r.getResReservedDt().toLocalTime())
//            .collect(Collectors.toList());
//        all.removeAll(booked);
//        return all;
//    }

	/**
	 * 2) 예약 생성
	 */
//    @Transactional
//    public Reservation createReservation(String userId, String intrId, LocalDateTime dt) {
//        Reservation r = Reservation.builder()
//            .resUserId(userId)
//            .resIntrId(intrId)
//            .resReservedDt(dt)
//            .resStatus(ResStatus.pending)
//            .resCreateDt(LocalDateTime.now())
//            .build();
//        return repo.save(r);
//    }

	@Transactional(readOnly = true)
	public List<LocalTime> findAvailableSlots(String interviewerId, LocalDate date) {
		// ① 9시~17시 정각 슬롯 목록 생성
		List<LocalTime> all = Stream.iterate(LocalTime.of(9, 0), t -> t.plusHours(1)).limit(9)
				.collect(Collectors.toList());

		// ② DB에서 해당 면접관·날짜에 이미 예약된 항목만 조회
		LocalDateTime start = date.atTime(9, 0);
		LocalDateTime end = date.atTime(17, 0);
		List<LocalTime> booked = repo.findAllByResIntrIdAndResReservedDtBetween(interviewerId, start, end).stream()
				.map(r -> r.getResReservedDt().toLocalTime()).collect(Collectors.toList());

		// ③ 전체 슬롯에서 이미 예약된 시간 제거
		all.removeAll(booked);
		return all;
	}

	@Transactional
    public ReservationDTO book(ReservationDTO dto) {
        // 1) 엔티티로 변환
        Reservation entity = Reservation.builder()
            .resUserId(       dto.getUserId())
            .resIntrId(       dto.getInterviewerId())
            .resReservedDt(   dto.getDateTime())
            .resStatus(       ResStatus.pending)      // 최초 상태
            .resCreateDt(     LocalDateTime.now())
            .build();

        // 2) 저장
        Reservation saved = repo.save(entity);

        // 3) DTO에 PK, 상태 채워서 리턴
        dto.setResId(    saved.getResId());
        dto.setStatus(   saved.getResStatus().name());
        return dto;
}
}
