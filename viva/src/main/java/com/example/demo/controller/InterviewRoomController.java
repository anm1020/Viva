package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.dto.InterviewRoomDTO;
import com.example.demo.service.InterviewRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class InterviewRoomController {

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
	public String newMeetingRoom(Model model) {

		return "webRtc/interview-new";
	}

	// 방 생성 후 목록 페이지 이동
	@RequestMapping("/meeting/createRoom")
	public String newMeetingRoom(InterviewRoomDTO room,
								Model model) {

		String test_memId = "rtc_test1";
		String test_intrId = "rtc_test2";
		
		 InterviewRoomDTO dto = InterviewRoomDTO.builder()
		            .intrRoomTitle(room.getIntrRoomTitle())
		            .hostId(test_intrId)
		            .statusCd("waiting")
		            .build();

		InterviewRoomDTO newRoom = service.createRoom(dto);

		return "redirect:/meeting/list";
	}

	// 방 목록 페이지
	@RequestMapping("/meeting/list")
	public String listMeetingRoom(Model model) {

		List<InterviewRoomDTO> roomList = service.getAllRooms(); // 방 목록 리스트

		for(InterviewRoomDTO room : roomList) {
			System.out.println(room.getIntrRoomTitle());
		}
		
		model.addAttribute("roomList", roomList);
		return "webRtc/interview-list";
	}
}
