package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRoomDTO {
    private Integer intrRoomId;
    private String intrRoomTitle;
    private String hostId;
    private LocalDateTime createdDt;
    private LocalDateTime startedDt;
    private LocalDateTime endedDt;
    private String statusCd;
    private String roomPw;            // 방 비밀번호
    private Integer participantCount; // 참여 인원
}
