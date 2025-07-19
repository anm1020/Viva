package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.entity.Review;
import com.example.demo.model.entity.Reviewboard;
import com.example.demo.model.entity.Users;
import com.example.demo.service.ReviewboardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviewboard")
public class ReviewboardController {

    private final ReviewboardService reviewboardService;

    // 1) 리뷰 목록
    @GetMapping("/list")
    public String reviewList(Model model, Principal principal) {
        String loginUserId = principal.getName();
        List<Reviewboard> reviews = reviewboardService.findAll();
        model.addAttribute("reviews", reviews);
        model.addAttribute("loginUserId", loginUserId);
        return "reviewboard/list";
    }


    // 2) 리뷰 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("reviewboard", new Reviewboard());
        return "reviewboard/write";
    }

    // 3) 리뷰 저장 처리
    @PostMapping("/write")
    public String writeSubmit(Reviewboard reviewboard, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        reviewboard.setUser(user);
        reviewboardService.saveReview(reviewboard);
        return "redirect:/reviewboard/list";
    }

    // 4) 리뷰 상세 페이지
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Reviewboard review = reviewboardService.getReviewById(id);
        model.addAttribute("review", review);
        return "reviewboard/detail";
    }

    // 1) 수정 폼 보여주기 (GET)
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long reviewId, Model model) {
        Reviewboard reviewboard = reviewboardService.findById(reviewId);
        model.addAttribute("reviewboard", reviewboard);
        return "reviewboard/edit";  // 수정 폼 뷰 이름
    }

    // 2) 수정 처리 (POST)
    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute Reviewboard reviewboard) {
        reviewboardService.updateReview(reviewboard);
        return "redirect:/reviewboard/list";  // 수정 후 목록 페이지로 리다이렉트
    }
    
    // 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") Long reviewId) {
        reviewboardService.deleteById(reviewId);
        return "redirect:/reviewboard/list";
    }

}
