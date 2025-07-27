//package com.example.demo.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import com.example.demo.service.InterviewerService;
//import lombok.RequiredArgsConstructor;
//
//@Controller
//@RequiredArgsConstructor
//public class MainController {
//    private final InterviewerService interviewerService;
//
//    @GetMapping("/main")
//    public String showMainPage(Model model) {
//        model.addAttribute("interviewerList", interviewerService.getInterviewerList());
//        return "main";  // → templates/main.html
//    }
//}
