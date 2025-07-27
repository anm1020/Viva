package com.example.demo.repository;

import com.example.demo.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	
	/**
	 * Notice 엔티티에 대한 JPA 리포지토리
	 */
	    
	    /**
	     * 전체 공지를 생성일 기준 내림차순으로 조회
	     */
	    List<Notice> findAllByOrderByCreatedAtDesc();

	    /**
	     * 최신 공지 3개만 조회
	     */
	    List<Notice> findTop3ByOrderByCreatedAtDesc();
	
	    
	    // 상단고정(isFixed=Y)인 공지글만& 최신순 3개
	    List<Notice> findTop3ByIsFixedOrderByCreatedAtDesc(String isFixed);

	    
	
//	// 기본 save, findAll, findById 등 CRUD 모두 제공
//	
//	// 최신순 정렬로 전체 조회
//	List<Notice> findAllByOrderByCreatedAtDesc();
//	
//	// 게시판에 최신 공지 3개 띄우기
//	List<Notice> findTop3ByOrderByCreatedAtDesc();
//	
////    // 상단고정 우선, 최신순 3개만 조회
////    List<Notice> findTop3ByDelYnOrderByIsFixedDescCreatedAtDesc(String delYn);
////
////    // 전체 목록(삭제되지 않은)
////    List<Notice> findByDelYnOrderByIsFixedDescCreatedAtDesc(String delYn);
}
