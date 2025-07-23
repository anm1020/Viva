package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import com.example.demo.model.entity.PointExchange;
import com.example.demo.model.entity.Users;
import com.example.demo.service.PaymentService;
import com.example.demo.service.PointExchangeService;
import com.example.demo.service.PointService;
import com.example.demo.service.ReservationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService service;
	private final ReservationService reservationService;
	private final PointExchangeService pointExchangeService;

	// application.properties 에서 읽어 오는 impKey
	@Value("${portone.imp-key}")
	private String impKey;

	/**
	 * 1) 결제 페이지 열기 charge.html 에서는 th:text="${impKey}", th:text="${payId}",
	 * th:text="${amount}" 사용
	 */
	@GetMapping("/charge")
	public String showChargePage(@RequestParam("resId") Long resId,HttpSession session,
								Model model) {

		System.out.println("결제창 진입: resId=" + resId); // ← 로그로 확
		

	    String userId = (String) session.getAttribute("userId");
	    if (userId == null) return "redirect:/login";

	    // 사용자 정보 추가로 꺼낼 수 있다면 아래처럼 처리
	    Users user = (Users) session.getAttribute("user");
	    if (user != null) {
	        model.addAttribute("userName", user.getUserName());
	        model.addAttribute("userTel", user.getUserNum());
	    }

		model.addAttribute("resId", resId);
		model.addAttribute("impKey", impKey); // PortOne IMP 키
		
		return "payment/charge";
	}

	/**
	 * 2) 결제 검증 & 저장 (AJAX POST /payment/verify)
	 */
	@PostMapping("save")
	@ResponseBody
	public ResponseEntity<Integer> savePayment(@RequestBody Payment payment, HttpSession session) {

		String user_id = (String)session.getAttribute("userId");
		 // 1) 세션에서 userId 꺼내오기 (생략)
	    //String user_id = "s";
	    payment.setUserId(user_id);
	    

	    // 결제 타입이 없는 경우 기본값 지정 (예: CARD)
	    if(payment.getPayType() == null) {
	        payment.setPayType(Payment.PayType.CARD);
	    }
	    
	    if(payment.getPayStatus() == null) {
	        payment.setPayStatus(Payment.PayStatus.paid);
	    }

	    System.out.println("payType: " + payment.getPayType());
	    System.out.println("Saving payment: " + payment);

	    // 2) 결제 정보 저장
	    Payment result = service.savePayment(payment);

	    // 3) 결제 성공 시 예약 상태 변경
	    if (result != null) {
	        try {
	            // payment.getResId() 에 res_id 가 담겨 있으니, 그걸 서비스에 바로 넘깁니다
	            reservationService.confirmReservation(payment.getResId());
	            System.out.println("예약 상태 변경 완료");
	        } catch (Exception e) {
	            System.out.println("예약 상태 변경 실패: " + e.getMessage());
	        }
	    }

	    return ResponseEntity.ok(1);
	}

	/**
	 * 3) 결제 결과 페이지
	 */
	@GetMapping("/result")
	public String showResultPage(@RequestParam("amount") Double amount,
			@RequestParam(value = "error", required = false) String error, Model model) {
		model.addAttribute("amount", amount);
		model.addAttribute("error", error);
		return "payment/result";
	}
   
	// 결제내역
	@GetMapping("/mypage/paymentList")
	public String loadPaymentListFragment(Model model, Principal principal) {
	    String userId = principal.getName();
	    List<Payment> payments = service.getPaymentsByUserId(userId);
	    
	 // 포인트 환전 내역 조회
	    List<PointExchange> exchanges = pointExchangeService.getUserExchangeList(userId);
	    if (exchanges == null) {
	        exchanges = new ArrayList<>();
	    }
	    model.addAttribute("payments", payments);
	    model.addAttribute("exchanges", exchanges);
	    return "mypage/paymentListFragment :: paymentListFragment";
	}
    
 

}
