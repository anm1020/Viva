package com.example.demo.model.dto;

import java.time.LocalDateTime;

//import com.example.demo.model.entity.User.UserRole;
//import com.example.demo.model.entity.User.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String userId;
    private String userPass;
    private String userName;
    private String userGender;
    private String userBirth;
    private String userNum;
    private String userEmail;
    private String userAdd;
    private String userAddDetail;
    private String userRole;
    private String userSkill;
    private String userCareer;
    private String userOuth;
    private String userType;
    private LocalDateTime createdDt;

//    public enum UserRole {
//        mem, intr
//    }
//
//    public enum UserType {
//        Y, N
//    }
    
 // 추가
    // 비밀번호 확인
    private String userPassConfirm;

    // 전화번호 분리 필드
    private String phonePrefix;
    private String phoneMiddle;
    private String phoneLast;

    // 이메일 분리 필드
    private String emailId;
    private String emailDomain;
    
}
