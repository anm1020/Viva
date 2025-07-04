package com.example.demo.model.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interview_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)			// mysql auto_increment 연결
	@Column(name = "intr_room_id")
	private Integer intrRoomId;

	@Column(name = "intr_room_title", length = 200)
	private String intrRoomTitle;

	@Column(name = "host_id", length = 50)
	private String hostId;

	@Column(name = "created_dt")
	private LocalDateTime createdDt;

	@Column(name = "started_dt")
	private LocalDateTime startedDt;

	@Column(name = "ended_dt")
	private LocalDateTime endedDt;

	@Column(name = "status_cd", length = 20)
	private String statusCd;

	// insert 수행시 createDt값 자동으로 저장
	@PrePersist
    public void prePersist() {
        this.createdDt = LocalDateTime.now();
    }
	
}
