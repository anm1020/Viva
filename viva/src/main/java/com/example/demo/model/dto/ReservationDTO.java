//package com.example.demo.model.dto;
//
//import java.sql.Timestamp;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Data 
//@NoArgsConstructor 
//@AllArgsConstructor 
//@Builder
//public class ReservationDTO {
//	   // ── 요청용 필드 ───────────────────────────
//    private String intrId;  // 면접관 ID (요청 시 필수)
//    private String date;    // yyyy-MM-dd
//    private String time;    // HH:mm
//
//    // ── 응답·조회용 필드 ───────────────────────
//    private Long   resId;         // 저장 후 반환할 PK
//    private Timestamp resCreateDt;// 생성 시각
//    private String status;        // pending/confirmed/cancelled
//}
