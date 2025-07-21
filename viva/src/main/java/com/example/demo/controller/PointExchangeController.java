package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.entity.PointExchange;
import com.example.demo.model.entity.Users;
import com.example.demo.service.PointExchangeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PointExchangeController {

    private final PointExchangeService pointExchangeService;

    // 환전 신청 POST API
    @PostMapping("/point/exchange/request")
    public ResponseEntity<?> requestExchange(@RequestBody PointExchange pointExchange, HttpSession session) {
        try {
            // 세션에서 로그인 사용자 아이디 꺼내서 엔티티에 세팅
            String userId = ((Users) session.getAttribute("user")).getUserId();
            pointExchange.setUserId(userId);
            System.out.println("bank = " + pointExchange.getBank());  // 추가 로그

            // 신청 시간 설정
            pointExchange.setRequestedAt(LocalDateTime.now());

            pointExchangeService.requestExchange(pointExchange);

            return ResponseEntity.ok(new ApiResponse(true, "환전 신청이 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    //서버에서 처리 성공 여부와 메시지를 클라이언트에 전달
    public static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    //조회 
    @GetMapping("/mypage/exchange-list")
    public String exchangeList(Model model, HttpSession session) {
        String userId = ((Users) session.getAttribute("user")).getUserId();

        List<PointExchange> exchanges = pointExchangeService.getUserExchangeList(userId);
        if (exchanges == null) {
            exchanges = new ArrayList<>();
        }
        model.addAttribute("exchanges", exchanges);

        return "mypage/exchangeList"; // 뷰 이름 (thymeleaf 템플릿)
    }
    
    @GetMapping("/mypage/payment-list-fragment")
    public String paymentListFragment(Model model, HttpSession session) {
        System.out.println("paymentListFragment 호출됨");
        String userId = ((Users) session.getAttribute("user")).getUserId();
        System.out.println("userId = " + userId);

        List<PointExchange> exchanges = pointExchangeService.getUserExchangeList(userId);
        if (exchanges == null) {
            exchanges = new ArrayList<>();
        }
        model.addAttribute("exchanges", exchanges);

        return "mypage/paymentListFragment :: paymentListFragment";
    }
    
    @GetMapping("/mypage/exchange-list-fragment")
    public String exchangeListFragment(Model model, HttpSession session) {
    	System.out.println("paymentListFragment 호출됨");
        String userId = ((Users) session.getAttribute("user")).getUserId();
        System.out.println("userId = " + userId);

        List<PointExchange> exchanges = pointExchangeService.getUserExchangeList(userId);
        if (exchanges == null) {
            exchanges = new ArrayList<>();
        }
        model.addAttribute("exchanges", exchanges);

        return "mypage/exchangeList :: fragment";
    }
    
    // 마이페이지 메인 뷰 (부모 뷰) : exchanges 포함 추가
    @GetMapping("/mypage/memMypage")
    public String memMypage(Model model, HttpSession session) {
        String userId = ((Users) session.getAttribute("user")).getUserId();

        List<PointExchange> exchanges = pointExchangeService.getUserExchangeList(userId);
        if (exchanges == null) {
            exchanges = new ArrayList<>();
        }
        model.addAttribute("exchanges", exchanges);

        // 다른 마이페이지에 필요한 데이터도 여기에 추가하세요
        // 예) 결제내역, 예약내역 등

        return "mypage/memMypage";
    }
    
    @GetMapping("/mypage/intrMypage")
    public String intrMypage(Model model, HttpSession session) {
        String userId = ((Users) session.getAttribute("user")).getUserId();

        List<PointExchange> exchanges = pointExchangeService.getUserExchangeList(userId);
        if (exchanges == null) {
            exchanges = new ArrayList<>();
        }
        model.addAttribute("exchanges", exchanges);

        // intrMypage에 필요한 다른 데이터도 추가 가능
        // 예) 면접관 관련 예약 내역, 일정 등

        return "mypage/intrMypage";
    }

}
