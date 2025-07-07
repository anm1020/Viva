package com.example.demo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

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
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.InterviewerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InterviewerController {

    private final InterviewerService interviewerService;
    private final ReviewRepository reviewRepository;
    
    // 리스트 페이지 
    @GetMapping("/interviewer")
    public String showIntrList(Model model) {
        List<InterviewerDTO> interviewers = interviewerService.getInterviewerList();
        model.addAttribute("interviewers", interviewers);
        return "interviewer/List";
    }
    
    // 면접관 작성 페이지
    @GetMapping("/interviewer/form")
    public String showForm() {
        return "interviewer/form"; // 등록 페이지 템플릿
    }
    // 면접관 저장 처리
    @PostMapping("/interviewer/save")
    public String saveInterviewer(@RequestParam String userId,
                                  @RequestParam(required = false) String intrIntro,
                                  @RequestParam(required = false) String intrSkills,
                                  @RequestParam(required = false) String intrImage,
                                  @RequestParam(required = false) Integer intrPrice,
                                  @RequestParam(required = false) List<String> intrCateList) {

        Interviewer interviewer = new Interviewer();
        interviewer.setUserId(userId);
        interviewer.setIntrIntro(intrIntro);
        interviewer.setIntrSkills(intrSkills);
        interviewer.setIntrImage(intrImage);
        interviewer.setIntrPrice(intrPrice);

        if (intrCateList != null && !intrCateList.isEmpty()) {
            String joinedCate = String.join(",", intrCateList); // 예: "WEB,FRONT,BACK"
            interviewer.setIntrCate(joinedCate);
        }

        interviewerService.save(interviewer);

        return "redirect:/interviewer";
    }

    @GetMapping("/interviewer/{intrId}")
    public String getInterviewerDetail(@PathVariable("intrId") Long intrId, Model model) {
        InterviewerDetailDTO detail = interviewerService.getInterviewerDetail(intrId);
        model.addAttribute("interviewer", detail);
        // 리뷰 리스트 추가
        List<Review> reviews = reviewRepository.findByIntrIdOrderByCreatedDtDesc(intrId.intValue());
        model.addAttribute("reviews", reviews);
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
