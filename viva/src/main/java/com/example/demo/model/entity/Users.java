package com.example.demo.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "user_pass", nullable = false, length = 100)
    private String userPass;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "user_gender", length = 20)
    private String userGender;

    @Column(name = "user_birth", length = 30)
    private String userBirth;

    @Column(name = "user_num", length = 20)
    private String userNum;

    @Column(name = "user_email", length = 100)
    private String userEmail;

    @Column(name = "user_add", length = 200)
    private String userAdd;

    @Column(name = "user_add_detail", length = 200)
    private String userAddDetail;

//  @Enumerated(EnumType.STRING)
    @Column(name = "user_role", columnDefinition = "ENUM('mem','intr','admin')")
    private String userRole;

    @Column(name = "user_skill", length = 3000)
    private String userSkill;

    @Column(name = "user_career", length = 255)
    private String userCareer;
    
    // 면접관 계정 활성화 상태 - 초기 가입시 N, 관리자 확인 후 Y 전환		// DEFAULT 'N'" value="N"
    @Column(name = "user_outh", length = 1, columnDefinition = "ENUM('Y', 'N') DEFAULT 'N'")
    private String userOuth;

//  @Enumerated(EnumType.STRING)							 DEFAULT 'Y'
    @Column(name = "user_type", columnDefinition = "ENUM('Y', 'N') DEFAULT 'Y'")	// 계정 활성화 상태
    private String userType;

    @CreationTimestamp
    @Column(name = "created_dt", updatable = false)
    private LocalDateTime createdDt;
   

//    public enum UserRole {	 필요없음
//        mem, intr
//    }
    
    public enum UserType {
        Y, N
    }
   
    // 비밀번호 확인
    @Transient
    private String userPassConfirm;

    // 전화번호 분리 필드
    @Transient
    private String phonePrefix;
    @Transient
    private String phoneMiddle;
    @Transient
    private String phoneLast;

    // 이메일 분리 필드
    @Transient
    private String emailId;
    @Transient
    private String emailDomain;
    
    // 면접관 경력
    @Transient
    private String userCareer2;
    
}
