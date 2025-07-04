package com.example.demo.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {

    private String user_id;
    private String user_pass;
    private String user_name;
    private String user_gender;
    private LocalDate user_birth;
    private String user_num;
    private String user_email;
    private String user_add;
    private String user_add_detail;
    private String user_role;
    private String user_skill;
    private String user_career;
    private String user_outh;
    public enum userType {
        Y, N
    }
    private userType user_type;
    private LocalDateTime created_dt;
    
    // 추가
    private String userpassconfirm;
    private String phoneprefix;
    private String phonemiddle;
    private String phonelast;
    private String emailid;
    private String emaildomain;
   
}
