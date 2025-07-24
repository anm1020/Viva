package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.entity.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.security.CustomUserDetails;

@Controller
public class MeetingPageController {
	
	@Autowired
	private UsersRepository usersRepository;
	
	@GetMapping("/interviewPage") // 또는 "/meeting/main" 등
	public String interviewSPA(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		String userId = userDetails.getUsername(); // 또는 getUserId() 등 커스텀 정의에 따라
		Users user = usersRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + userId));

		System.out.println(userId);
		System.out.println(user.getUserRole());

		model.addAttribute("userId", userId);
		model.addAttribute("userRole", user.getUserRole());
		return "webRtc/interviewPage"; // templates/webRtc/interview.html
	}

	@GetMapping("/demoTest")
	public String demoTest() {
		return "webRtc/webRtcDemoTest";
	}

}
