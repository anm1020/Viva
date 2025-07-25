package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.InterviewRoomDTO;
import com.example.demo.model.entity.InterviewRoom;
import com.example.demo.repository.InterviewRoomRepository;
import com.example.demo.service.InterviewRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meeting")
public class MeetingRoomController {

	private final InterviewRoomService service;
	private final InterviewRoomRepository roomRepository;

	// 방 생성
	@PostMapping("/roomcreate")
	public ResponseEntity<?> createRoom(@RequestBody InterviewRoomDTO room,
				Model model) {
		System.out.println("방생성자 : " + room.getHostId());
		
		// 추후 방생성자의 role이 취준생인지 면접관인지 체크하고 면접관일 경우에만 방생성 
		
		room.setStatusCd("waiting");
		
		try {
			// 면접방 + 채팅방 동시에 생성
			InterviewRoomDTO newRoom = service.createRoomWithText(room);

			// (선택) 생성된 방 정보 model에 담아 redirect 가능
			model.addAttribute("newRoom", newRoom);
			return ResponseEntity.ok(newRoom);

		} catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("❌ 면접 방 생성 중 오류 발생: " + e.getMessage());
	    }

	}

	// 방 목록
	@GetMapping("/roomlist")
	public ResponseEntity<List<InterviewRoomDTO>> listRooms() {
		List<InterviewRoomDTO> roomList = service.getAllRooms();
		return ResponseEntity.ok(roomList);
	}
	
	
	@PostMapping("/{roomId}/end")
	public ResponseEntity<?> endRoom(@PathVariable("roomId") Integer roomId, @RequestBody Map<String, String> body) {
	    String inputPw = body.get("roomPw");
	    InterviewRoom room = roomRepository.findById(roomId)
	                         .orElseThrow(() -> new RuntimeException("방 없음"));

	    if (!room.getRoomPw().equals(inputPw)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호 불일치");
	    }
	    room.setStatusCd("ended");
	    roomRepository.save(room);
	    return ResponseEntity.ok().build();
	}
	
}
