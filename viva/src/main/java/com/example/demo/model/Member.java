package com.example.demo.model;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "members")
public class Member {

	@Id   // Id 어노테이션 바로 아래 쪽이 primary key 설정됨
//	private int mem_no;     //      INT AUTO_INCREMENT PRIMARY KEY,
	
	@Column(length=50, nullable=false)
	private String mem_id;       		   // VARCHAR(50) NOT NULL,
	
	@Column(length=100, nullable=false)
	private String mem_pass;      		  // VARCHAR(100) NOT NULL,
		
	@Column(length=50, nullable=false)
	private String mem_name;      		  // VARCHAR(50) NOT NULL,
	
	private String mem_gender;     		 // VARCHAR(10),
	
	private String mem_birth;      		 // DATE,
	private int mem_num;        		 // VARCHAR(20),
    private String mem_email;     		  // VARCHAR(100),
    private String mem_add;        		 // VARCHAR(200),
    private String mem_add_detail; 		 // VARCHAR(200),
    private String mem_role;       		 // VARCHAR(20), -- ex: applicant, admin, ai, interviewer
    private String mem_skill;      		 // VARCHAR(100),
    
    private String mem_type;      	     // VARCHAR(10) DEFAULT 'N'
    
    @CreationTimestamp					 // insert 한 시간이 입력됨(가입일)
    private Timestamp created_dt;        // DATETIME DEFAULT CURRENT_TIMESTAMP
	
}
