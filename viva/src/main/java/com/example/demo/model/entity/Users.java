package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
public class Users {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;
    
    @Column(name ="user_skill")
    private String userSkill;

    @Column(name = "user_career")
    private String userCareer;

    // 필요하면 추가 필드
    // @Column(name = "user_email")
    // private String userEmail;
}
