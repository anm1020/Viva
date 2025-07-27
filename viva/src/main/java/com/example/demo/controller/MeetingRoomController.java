package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<?> createRoom(@RequestBody Map<String, Object> payload,
										Model model) {
		try {
			
			// InterviewRoomDTO 수동 생성
	        InterviewRoomDTO room = InterviewRoomDTO.builder()
	                .intrRoomTitle((String) payload.get("intrRoomTitle"))
	                .hostId((String) payload.get("hostId"))
	                .roomPw((String) payload.get("roomPw"))
	                .participantCount(Integer.parseInt(payload.get("participantCount").toString()))
	                .resId(payload.get("resId") != null ? Long.valueOf(payload.get("resId").toString()) : null)
	                .statusCd("waiting")
	                .build();

	        // reservedDate / reservedTime 별도 추출
	        String reservedDateStr = (String) payload.get("reservedDate");
	        String reservedTimeStr = (String) payload.get("reservedTime");

	        // "2025-07-30T10:00" 형식으로 만들어 LocalDateTime으로 파싱
            LocalDateTime startedDt = LocalDateTime.parse(reservedDateStr + "T" + reservedTimeStr + ":00");

            room.setStartedDt(startedDt); // 엔티티에 started_dt로 저장될 값 설정
	        
			// 면접방 + 채팅방 동시에 생성
			InterviewRoomDTO newRoom = service.createRoomSafely(room);

			// (선택) 생성된 방 정보 model에 담아 redirect 가능
			model.addAttribute("newRoom", newRoom);
			return ResponseEntity.ok(newRoom);

		} catch (IllegalStateException e) {
	        return ResponseEntity.badRequest().body("❌ " + e.getMessage());
	    } catch (Exception e) {
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
	
	// 방 정보
	@GetMapping("/roominfo")
    public InterviewRoomDTO getRoomInfo(@RequestParam("resId") Integer resId) {
		InterviewRoomDTO roomInfo = service.getRoomByResId(resId);  
		System.out.println("방제목: " + roomInfo.getIntrRoomTitle());
		System.out.println("방비밀번호: " + roomInfo.getRoomPw());
		
		return roomInfo;
    }
	
	// 참가자 입장 (인원 +1)
    @PostMapping("/room/{roomId}/join")
    public void joinRoom(@PathVariable("roomId") Integer roomId) {
        service.increaseCurrentParticipantCount(roomId);
    }

    // 참가자 퇴장 (인원 -1)
    @PostMapping("/room/{roomId}/leave")
    public void leaveRoom(@PathVariable("roomId") Integer roomId) {
        service.decreaseCurrentParticipantCount(roomId);
    }
	
	// 방 삭제
	@PostMapping("/{roomId}/end")
	public ResponseEntity<?> endRoom(@PathVariable("roomId") Integer roomId, @RequestBody Map<String, String> body) {
	    String inputPw = body.get("roomPw");
	    InterviewRoom room = roomRepository.findById(roomId)
	                         .orElseThrow(() -> new RuntimeException("방 없음"));

	    if (!room.getRoomPw().equals(inputPw)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호 불일치");
	    }
	    room.setStatusCd("ended");
	    room.setEndedDt(LocalDateTime.now()); 
	    roomRepository.save(room);
	    return ResponseEntity.ok().build();
	}
	
}
