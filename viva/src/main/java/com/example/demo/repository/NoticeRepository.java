package com.example.demo.repository;

import com.example.demo.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	
	// 기본 save, findAll, findById 등 CRUD 모두 제공
	
	// 최신순 정렬로 전체 조회
	List<Notice> findAllByOrderByCreatedAtDesc();
	
//    // 상단고정 우선, 최신순 3개만 조회
//    List<Notice> findTop3ByDelYnOrderByIsFixedDescCreatedAtDesc(String delYn);
//
//    // 전체 목록(삭제되지 않은)
//    List<Notice> findByDelYnOrderByIsFixedDescCreatedAtDesc(String delYn);
}
