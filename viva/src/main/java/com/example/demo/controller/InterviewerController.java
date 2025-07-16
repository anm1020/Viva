package com.example.demo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.dto.InterviewerDTO;
import com.example.demo.model.dto.InterviewerDetailDTO;
import com.example.demo.model.entity.Interviewer;
import com.example.demo.model.entity.Review;
import com.example.demo.model.entity.Users;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.InterviewerService;
import com.example.demo.service.ReservationService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InterviewerController {

    private final InterviewerService interviewerService;
    private final ReservationService reservationService;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper; 
    
    // 리스트 페이지 
    @GetMapping("/interviewer")
    public String showIntrList(@RequestParam(value = "intrCate", required = false) String intrCate, Model model) {
        List<InterviewerDTO> interviewers;
        if (intrCate != null && !intrCate.isEmpty()) {
            // 여러 카테고리(콤마구분) 검색 지원
            String[] categories = intrCate.split(",");
            interviewers = new java.util.ArrayList<>();
            for (String cate : categories) {
                interviewers.addAll(interviewerService.getInterviewerListByCategory(cate));
            }
            // 중복 제거
            interviewers = interviewers.stream().distinct().toList();
        } else {
            // 전체 목록
            interviewers = interviewerService.getInterviewerList();
        }
        if (interviewers == null) {
            interviewers = java.util.Collections.emptyList();
        }
        model.addAttribute("interviewers", interviewers);
        return "interviewer/List";
    }
    
    // 면접관 작성 페이지
    @GetMapping("/interviewer/form")
    public String showForm(Principal principal, Model model) {
        String userId = principal.getName();
        Users user = userService.findByUserId(userId)
        		.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));;
                
        if (user == null || !"intr".equals(user.getUserRole())) {
            // 면접관이 아니면 접근 불가
            return "redirect:/interviewer?error=not_intr";
        }
        model.addAttribute("user", user);
        return "interviewer/form"; // 등록 페이지 템플릿
    }
    // 면접관 저장 처리
    @PostMapping("/interviewer/save")
    public String saveInterviewer(Principal principal,
                                  @RequestParam(name = "intrIntro", required = false) String intrIntro,
                                  @RequestParam(name = "intrImage", required = false) String intrImage,
                                  @RequestParam(name = "intrPrice", required = false) Integer intrPrice,
                                  @RequestParam(name = "intrCate", required = false) String intrCate,
                                  @RequestParam(name = "intrContent", required = false) String intrContent) {
        String userId = principal.getName();
        Users user = userService.findByUserId(userId)
        		.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));;
          
        if (user == null || !"intr".equals(user.getUserRole())) {
            // 면접관이 아니면 저장 불가
            return "redirect:/interviewer?error=not_intr";
        }
        Interviewer interviewer = new Interviewer();
        interviewer.setUserId(userId);
        interviewer.setIntrIntro(intrIntro);
        interviewer.setIntrImage(intrImage);
        interviewer.setIntrPrice(intrPrice);
        interviewer.setIntrCate(intrCate);
        interviewer.setIntrContent(intrContent);
        // users 테이블 정보는 JOIN으로 가져오므로 여기서 설정하지 않음
        interviewerService.save(interviewer);
        return "redirect:/interviewer";
    }

    @GetMapping("/interviewer/{intrId}")
    public String getInterviewerDetail(@PathVariable("intrId") Long intrId, Model model, HttpSession session
    		)throws JsonProcessingException {
        InterviewerDetailDTO detail = interviewerService.getInterviewerDetail(intrId);
        model.addAttribute("interviewer", detail);
        // 리뷰 리스트 추가
        List<Review> reviews = reviewRepository.findByIntrIdOrderByCreatedDtDesc(intrId.intValue());
        model.addAttribute("reviews", reviews);
        
        // 이슬이 예약 폼에 필요한 데이터
     // 예약 및 비활성화 슬롯 Map 조회
        Map<String, List<String>> reservedSlots = reservationService.getReservedSlotsByIntrId(intrId.toString());
        Map<String, List<String>> disabledSlots = reservationService.getDisabledSlotsByIntrIdGrouped(intrId.toString());

        // JSON 변환
        String reservedSlotsJson = objectMapper.writeValueAsString(reservedSlots);
        String disabledSlotsJson = objectMapper.writeValueAsString(disabledSlots);

        model.addAttribute("intrId", intrId); // 숫자 PK (필요하면)
        model.addAttribute("intrUserId", detail.getUserId()); // users.user_id (문자열, 예약할 때 꼭 필요)
        model.addAttribute("reservedSlotsJson", reservedSlotsJson);
        model.addAttribute("disabledSlotsJson", disabledSlotsJson);

        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("userName", user.getUserName());
            model.addAttribute("userTel", user.getUserNum());
        }
        return "interviewer/detail";
    }

    // 리뷰 등록 
    @PostMapping("/interviewer/{intrId}/review")
    public String addReview(@PathVariable("intrId") Long intrId,
                            @RequestParam("score") BigDecimal score,
                            @RequestParam("content") String content,
                            Principal principal) {
        Review review = new Review();
        review.setIntrId(intrId.intValue());
        review.setScore(score);
        review.setContent(content);
        review.setUserId(principal.getName());
        reviewRepository.save(review);
        return "redirect:/interviewer/" + intrId;
    }

    // 리뷰 수정
    @PostMapping("/interviewer/{intrId}/review/{reviewNo}/edit")
    public String editReview(@PathVariable("intrId") Long intrId,
                             @PathVariable("reviewNo") Integer reviewNo,
                             @RequestParam("score") BigDecimal score,
                             @RequestParam("content") String content,
                             Principal principal) {
        Review review = reviewRepository.findById(reviewNo).orElse(null);
        if (review != null && review.getUserId().equals(principal.getName())) {
            review.setScore(score);
            review.setContent(content);
            reviewRepository.save(review);
        }
        return "redirect:/interviewer/" + intrId;
    }

    // 리뷰 삭제
    @PostMapping("/interviewer/{intrId}/review/{reviewNo}/delete")
    public String deleteReview(@PathVariable("intrId") Long intrId,
                               @PathVariable("reviewNo") Integer reviewNo,
                               Principal principal) {
        Review review = reviewRepository.findById(reviewNo).orElse(null);
        if (review != null && review.getUserId().equals(principal.getName())) {
            reviewRepository.delete(review);
        }
        return "redirect:/interviewer/" + intrId;
    }

}
