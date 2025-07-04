package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.InterviewRoomDTO;
import com.example.demo.service.InterviewRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meeting")
public class MeetingRoomController {

	private final InterviewRoomService service;

	// 방 생성
	@PostMapping("/roomcreate")
	public ResponseEntity<InterviewRoomDTO> createRoom(@RequestBody InterviewRoomDTO room) {
		String test_intrId = "rtc_test2";
		room.setHostId(test_intrId);
		room.setStatusCd("waiting");

		InterviewRoomDTO newRoom = service.createRoom(room);
		return ResponseEntity.ok(newRoom);
	}

	// 방 목록
	@GetMapping("/roomlist")
	public ResponseEntity<List<InterviewRoomDTO>> listRooms() {
		List<InterviewRoomDTO> roomList = service.getAllRooms();
		return ResponseEntity.ok(roomList);
	}
	
}
