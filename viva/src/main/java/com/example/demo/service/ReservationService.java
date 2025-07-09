package com.example.demo.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.IntrDisabled;
import com.example.demo.model.entity.Payment;
import com.example.demo.model.entity.Reservation;
import com.example.demo.repository.IntrDisabledRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.TestUserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Reservation(예약) 관련 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

	private final PaymentService paymentservice;
	private final PointService pointservice;
	private final TestUserRepository userRepo;
	private final ReservationRepository resRepo;
	private final PaymentRepository paymentRepo;
	private final IntrDisabledRepository intrDisabledRepo;

	// 저장
	@Transactional
	public Reservation save(Reservation res) {
		// 1) Users 엔티티 로드 (mem/intr 둘 다 users.user_id)
//        Users member      = userRepo.getReferenceById(memId);
//        Users interviewer = userRepo.getReferenceById(intrId);
//
//        // 2) Reservation 엔티티 빌드
//        Reservation r = Reservation.builder()
//            .memId(member)
//            .intrId(interviewer)
//            .resReservedDt(Timestamp.valueOf(dt))
//            .resStatus(Reservation.ResStatus.pending)
//            .build();

		// 3) 저장 후, 저장된 Reservation 반환
		return resRepo.save(res);
	}

	// 면접관 ID로 이미 예약된 날짜(yyyy-MM-dd)만 모아서 반환
	@Transactional(readOnly = true)
	public List<String> findReservedDatesByIntrId(String intrId) {
		return resRepo
				// 예약된 내역을 모두 꺼낸 뒤
				.findAllByIntrIdOrderByReservedDateDescReservedTimeDesc(intrId)
				// reservedDate 필드만 꺼내고
				.stream().map(Reservation::getReservedDate)
				// 중복 제거
				.distinct().collect(Collectors.toList());
	}

	// 회원(예약자) 기준 예약 내역 조회
	public List<Reservation> findReservationsByMemId(String userId) {
		// String으로 직접 조회
		// return resRepo.findAllByMemIdOrderByResReservedDtDesc(userId);
		// 예약일자+시간 내림차순 정렬 호출
		return resRepo.findAllByMemIdOrderByReservedDateDescReservedTimeDesc(userId);
	}

	// 결제 완료된 예약
	public List<Reservation> getPaidReservations(String userId) {
		// 1) paid 상태인 resId 목록 조회
		List<Long> paidResIds = paymentRepo.findPaidResIdsByUserId(userId);
		if (paidResIds.isEmpty()) {
			return Collections.emptyList();
		}
		// 2) 해당 resId 목록으로 Reservation 조회
		// return resRepo.findAllByResIdInOrderByResReservedDtDesc(paidResIds);
		return resRepo.findAllByResIdInOrderByReservedDateDescReservedTimeDesc(paidResIds);
	}

	// 결제 전(미완료)인 예약
	public List<Reservation> getPendingReservations(String userId) {
		// 1) 회원의 전체 예약
		List<Reservation> all = findReservationsByMemId(userId);
		// 2) 결제 완료된 ID 목록
		List<Long> paidResIds = paymentRepo.findPaidResIdsByUserId(userId);
		// 3) 전체에서 paidResIds 제외
		return all.stream().filter(r -> !paidResIds.contains(r.getResId())).collect(Collectors.toList());
	}

	// 면접관 기준 예약 내역 조회
	public List<Reservation> findReservationsByIntrId(String userId) {
		// String으로 직접 조회
		return resRepo.findAllByMemIdOrderByReservedDateDescReservedTimeDesc(userId);
	}

	// 카드 결제 후 상태 변경
	@Transactional
	public void confirmReservation(Long resId) {
		Reservation r = resRepo.findById(resId).orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다!"));
		r.setResStatus("confirmed"); // 엔티티의 상태만 바꿔주면 JPA가 자동으로 UPDATE 날려줍니다
	}

	// 포인트 결제 후 예약 상태 변경
	@Transactional
	public void markReservationAsPaid(Long resId) {
		Reservation r = resRepo.findById(resId).orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다!"));
		r.setResStatus("confirmed"); // 포인트 결제용

		// 💡 면접관 포인트 지급
		String intrId = r.getIntrId();
		int amount = paymentRepo.findByResId(resId).stream().filter(p -> p.getPayStatus() == Payment.PayStatus.paid)
				.findFirst().map(p -> p.getPayAmount().intValue()).orElse(0);

		pointservice.charge(intrId, amount); // 면접관에게 포인트 지급
		resRepo.save(r);
	}

	/**
	 * (1) 인터뷰어 ID 로 이미 예약된 슬롯(날짜 → 시간 목록)을 조회합니다. Map<날짜(yyyy-MM-dd), 예약된 시간 리스트>
	 */
	@Transactional(readOnly = true)
	public Map<String, List<String>> getReservedSlotsForInterviewer(String intrId) {
		List<Reservation> all = resRepo.findAllByIntrIdOrderByReservedDateDescReservedTimeDesc(intrId);

		return all.stream().collect(Collectors.groupingBy(Reservation::getReservedDate,
				Collectors.mapping(Reservation::getReservedTime, Collectors.toList())));
	}

	/**
	 * (2) 예약 ID 로 예약을 찾아 새 날짜·시간으로 업데이트(변경)합니다.
	 */
	@Transactional
	public Reservation reschedule(Long resId, String newDate, String newTime) {
		Reservation r = resRepo.findById(resId).orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다."));
		Map<String, List<String>> slots = getReservedSlotsForInterviewer(r.getIntrId());
		if (slots.getOrDefault(newDate, List.of()).contains(newTime)) {
			throw new RuntimeException("이미 해당 시간에 예약이 있습니다.");
		}
		r.setReservedDate(newDate);
		r.setReservedTime(newTime);
		return r; // Dirty Checking 으로 자동 저장
	}

	public Reservation findById(Long resId) {
		return resRepo.findById(resId).orElse(null); // 못찾으면 null
	}

	public Map<String, List<String>> getReservedSlotsByIntrId(String intrId) {
		// DB에서 해당 면접관의 모든 Reservation 조회
		List<Reservation> list = resRepo.findAllByIntrIdOrderByReservedDateDescReservedTimeDesc(intrId);
		// 날짜별로 묶어서 시간 리스트로
		return list.stream().collect(Collectors.groupingBy(Reservation::getReservedDate, LinkedHashMap::new,
				Collectors.mapping(Reservation::getReservedTime, Collectors.toList())));
	}

	/**
	 * ✅ 면접관의 ID를 기준으로 예약 목록을 조회하는 메서드 - 최신 예약부터 정렬해서 반환합니다
	 */
	public List<Reservation> getIntrReservations(String intrId) {
		return resRepo.findByIntrIdOrderByReservedDateDescReservedTimeDesc(intrId);
	}

	// 면접관이 선택한 날짜/시간을 비활성화로 저장
	public void saveDisabledDate(String intrId, String date, String time) {
		IntrDisabled disabled = new IntrDisabled();
		disabled.setIntrId(intrId);
		disabled.setDisabledDate(date);
		disabled.setDisabledTime(time);
		intrDisabledRepo.save(disabled);
	}

	// 면접관의 모든 비활성화 날짜/시간 조회
	public List<IntrDisabled> getDisabledSlots(String intrId) {
		return intrDisabledRepo.findByIntrId(intrId);
	}

	// 특정 날짜+시간이 비활성화 상태인지 체크
	public boolean isSlotDisabled(String intrId, String date, String time) {
		return intrDisabledRepo.existsByIntrIdAndDisabledDateAndDisabledTime(intrId, date, time);
	}

	// 면접관의 모든 비활성화 날짜/시간 리스트 반환
	public List<IntrDisabled> getDisabledDatesByIntrId(String userId) {
		return intrDisabledRepo.findByIntrId(userId);
	}

	// 면접관이 해당 날짜+시간을 비활성화했는지 체크
	public boolean isDisabled(String intrId, String date, String time) {
		return intrDisabledRepo.existsByIntrIdAndDisabledDateAndDisabledTime(intrId, date, time);
	}

	// 특정 면접관(intrId)이 예약 불가능하게 차단한 날짜/시간 목록
	public Map<String, List<String>> getDisabledSlotsByIntrIdGrouped(String intrId) {
		List<IntrDisabled> list = intrDisabledRepo.findByIntrId(intrId);
		return list.stream().collect(Collectors.groupingBy(IntrDisabled::getDisabledDate,
				Collectors.mapping(IntrDisabled::getDisabledTime, Collectors.toList())));
	}
	
	 // 비활성화 ID로 삭제
    public void deleteDisabledTime(Long id) {
        intrDisabledRepo.deleteById(id);
    }

//취소상태변
	@Transactional
	public void cancelReservation(Long resId) {
		Reservation reservation = resRepo.findById(resId).orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

		reservation.setResStatus("cancelled"); // ← 상태만 'cancelled'로 변경
		// JPA의 dirty checking에 의해 자동 update 됩니다.
	}

}
