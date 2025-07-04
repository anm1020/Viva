package com.example.demo.model.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationDTO {
    private Long   resId;         // 생성 후 응답용
    private String resUserId;     // 요청 시 필수
    private String resIntrId;     // 요청 시 필수
    private LocalDateTime resReservedDt; // 요청 시 필수
    private String resStatus;     // 응답 시 보여줄 상태 (optional)
    private String  interviewerId;
    private String  userId;
    private LocalDateTime dateTime;
    private String  status;
    
}
