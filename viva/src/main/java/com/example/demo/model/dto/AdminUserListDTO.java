package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserListDTO {
    private String userId;      // users.user_id (PK, VARCHAR로 가정)
    private String userName;    // users.user_name
    private long boardCount;    // 게시물 수
    private long reviewCount;   // 리뷰 수
    private LocalDateTime createdDt; // 가입일
    private String userType;    // 상태 (Y/N 등)
}
