package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextRoomDTO {
	
	private Integer textRoomId; // text_rooms.text_room_id
	private Integer intrRoomId; // interview_rooms.intr_room_id (연결된 영상방 ID)
	private String hostId; // users.user_id (host의 ID)
	private String textRoomTitle; // text_rooms.text_room_title
	private LocalDateTime createdDt; // 생성 일시
	private String statusCd; // 상태 코드
	
}
