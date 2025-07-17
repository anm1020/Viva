package com.example.demo.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.security.CustomUserDetails;


@Controller
public class MeetingPageController {
	@GetMapping("/interviewPage")  // 또는 "/meeting/main" 등
    public String interviewSPA(@AuthenticationPrincipal CustomUserDetails userDetails,
    						  Model model) {
		String userId = userDetails.getUsername(); // 또는 getUserId() 등 커스텀 정의에 따라
	    
		System.out.println(userId);
		
		model.addAttribute("userId", userId);
		
		return "webRtc/interviewPage"; // templates/webRtc/interview.html
    }
	
	@GetMapping("/demoTest")
	public String demoTest() {
		return "webRtc/webRtcDemoTest";
	}
	
}
