package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MeetingPageController {
	@GetMapping("/interviewPage")  // 또는 "/meeting/main" 등
    public String interviewSPA() {
        return "webRtc/interviewTest"; // templates/webRtc/interview-spa.html
    }
}
