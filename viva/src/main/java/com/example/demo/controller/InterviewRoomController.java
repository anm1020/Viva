package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.entity.Users;
import com.example.demo.service.InterviewRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class InterviewRoomController {

	@Autowired
	private final InterviewRoomService service;

	@GetMapping("/videoroom")
	public String showVideoRoomTest(Model model) {
		return "webRtc/videoroomtestThyme";
	}

	// 화상면접회의 메인 페이지
	@RequestMapping("/interview-main")
	public String interviewMain(Model model) {
		return "webRtc/interview-main";
	}

	// 방 만들기 페이지 이동
	// 추후 로그인 세션확인
	// user-type : intr이 아니면 해당 페이지로 이동하지 않고 목록이나 다른 페이지로 이동
	@RequestMapping("/meeting/new")
	public String newMeetingRoom(Model model, @AuthenticationPrincipal Users user) {

		model.addAttribute("user", user.getUserId());
		return "webRtc/interview-new";
	}

}
