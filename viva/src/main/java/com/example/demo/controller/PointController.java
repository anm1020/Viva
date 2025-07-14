package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.entity.Payment;
import com.example.demo.model.entity.Reservation;
import com.example.demo.model.entity.Users;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PointService;
import com.example.demo.service.ReservationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

	private final PointService service;
	private final ReservationService resservice;
	private final PaymentRepository paymentRepository;

	// ✅ HTML 뷰 반환 (충전 페이지로 이동)
	@GetMapping("/chargePage")
	public String chargePage(Model model, HttpSession session) {

		// 세션에서 로그인된 사용자 꺼냄
		Users user = (Users) session.getAttribute("user");

		if (user == null) {
			return "redirect:/login"; // 로그인 안 되어있으면 로그인 페이지로
		}

		int point = service.getPoint(user.getUserId());

		model.addAttribute("userId", user.getUserId());
		model.addAttribute("point", point);
		model.addAttribute("userTel", user.getUserNum()); // ✅ 전화번호
		model.addAttribute("userName", user.getUserName()); // ✅ 이름

		return "point/charge"; // templates/point/charge.html
	}

	// ✅ 결제 성공 후 저장 + 포인트 충전
	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<?> savePayment(@RequestBody Map<String, Object> payload, HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			return ResponseEntity.badRequest().body("로그인 필요");
		}
		int amount = ((Number) payload.get("payAmount")).intValue();

		// String userId = (String) payload.get("userId");
		// int amount = ((Number) payload.get("payAmount")).intValue();

		// 결제 엔티티 저장
		Payment payment = new Payment();
		payment.setPayResno((String) payload.get("payResno")); // 결제 고유 번호
		payment.setPayAmount(BigDecimal.valueOf(amount)); // 결제 금액
		payment.setResId(Long.parseLong(payload.get("resId").toString())); // 예약번호: 0이면 포인트 충전 전용
		payment.setPayType(Payment.PayType.CHARGE); // ★ 필수! 포인트 충전 타입 지정
		payment.setPayStatus(Payment.PayStatus.paid); // 결제 완료 상태 지정
		payment.setUserId(user.getUserId()); // 회원 ID도 꼭 넣어주세요!
		paymentRepository.save(payment);

		// 포인트 충전
		service.charge(user.getUserId(), amount);

		return ResponseEntity.ok(Map.of("amount", amount));
	}

	// ✅ 포인트 조회 API
	@GetMapping("/balance")
	@ResponseBody
	public ResponseEntity<?> getPoint(@RequestParam("userId") String userId, HttpSession session) {
		// int point = service.getPoint(userId);
		// return ResponseEntity.ok(point);
		Users user = (Users) session.getAttribute("user");
		if (user == null) {
			return ResponseEntity.badRequest().body("로그인 필요");
		}

		int point = service.getPoint(user.getUserId());
		return ResponseEntity.ok(point);
	}

	// 포인트 결제
	@PostMapping("/use")
	@ResponseBody
	public ResponseEntity<String> usePoint(@RequestParam("userId") String userId,
										   @RequestParam("amount") int amount,
										   @RequestParam("resId") Long resId, 
										   HttpSession session) {
		   // 1. 세션에서 사용자 확인
		 Users user = (Users) session.getAttribute("user");
		    if (user == null) {
		        return ResponseEntity.badRequest().body("로그인 필요");
		    }
		    // 2. 사용자 포인트 차감
		    boolean success = service.use(user.getUserId(), amount);
		    if (!success) {
		        return ResponseEntity.badRequest().body("포인트 부족");
		    } 

		    // 3. 예약 상태 변경 (confirmed)
		    resservice.markReservationAsPaid(resId);
		    
		 // 4. 면접관에게 포인트 지급
		    Reservation reservation = resservice.findById(resId);
		    if (reservation != null) {
		        String intrId = reservation.getIntrId();
		        service.charge(intrId, amount);  // 면접관 포인트 적립
		    }
		    // 포인트 결제니까 markReservationAsPaid 호출
		    //resservice.markReservationAsPaid(resId);
		    
		 // 5. 포인트 결제 내역도 payments 테이블에 저장
		    Payment payment = Payment.builder()
		    	        .userId(user.getUserId())                    // 회원 ID
		    	        .resId(resId)                                // 예약 ID
		    	        .payAmount(BigDecimal.valueOf(amount))       // 금액
		    	        .payType(Payment.PayType.POINT)              // 포인트 결제!
		    	        .payStatus(Payment.PayStatus.paid)           // 결제 완료 상태
		    	        .payResno("POINT_" + System.currentTimeMillis()) // 포인트 결제용 임의 결제 번호
		    	        .build();
		    
		    System.out.println("포인트 결제 내역 저장: " + payment);
		    paymentRepository.save(payment);

		    return ResponseEntity.ok("포인트 결제 완료 + 면접관 포인트 지급 완료");
}
}
