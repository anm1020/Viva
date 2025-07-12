package com.example.demo.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "board")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 게시글 번호 (PK)

	@Column(nullable = false, length = 200)
	private String title; // 제목

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content; // 내용

	@Column(name = "view_count", nullable = false)
	private Integer viewCount  = 0; // 조회수

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt; // 작성일

	@Column(name = "updated_at")
	private LocalDateTime updatedAt; // 수정일

	@Column(name = "user_id", length = 50)
	private String userId; // 작성자 ID

	@PrePersist
	public void onCreate() {
	    this.createdAt = LocalDateTime.now();       // 등록 시간 설정
	    this.updatedAt = this.createdAt;            // 수정 시간도 동일하게 설정
	}
	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();		// 수정 시간만 갱신
	}
	
	 // ✅ 댓글 리스트 연관관계 추가
   @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Comment> comments = new ArrayList<>();

}


