package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.entity.Payment;
import com.example.demo.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    // application.properties 에서 읽어 오는 impKey
    @Value("${portone.imp-key}")
    private String impKey;

    /**
     * 1) 결제 페이지 열기
     *    charge.html 에서는 th:text="${impKey}", th:text="${payId}", th:text="${amount}" 사용
     */
    @GetMapping("/charge")
    public String showChargePage(
        @RequestParam("intrId") String intrId,
        @RequestParam("intrName") String intrName,
        @RequestParam("date")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam("time")
        @DateTimeFormat(pattern = "HH:mm") LocalTime time,
        Model model
    ) {
        model.addAttribute("intrId",    1);
        model.addAttribute("intrName",  intrName);
        model.addAttribute("reservedDate", date);
        model.addAttribute("reservedTime", time);
        // 금액도 모델로 넘겨줄 수 있습니다. (예: 100)
        model.addAttribute("amount", 100);
        return "payment/charge";
    }

    /**
     * 2) 결제 검증 & 저장 (AJAX POST /payment/verify)
     */
    @PostMapping("/save")
    @ResponseBody
    public Integer savePayment(@RequestBody Payment payment) {
        System.out.println("payment in: " + payment);
        service.savePayment(payment);
        return 1;
    }


    /**
     * 3) 결제 결과 페이지
     */
    @GetMapping("/result")
    public String showResultPage(
            @RequestParam("amount") Double amount,
            @RequestParam(value = "error", required = false) String error,
            Model model
    ) {
        model.addAttribute("amount", amount);
        model.addAttribute("error", error);
        return "payment/result";
    }
}
