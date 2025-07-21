package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
	// 페이지처리
	Page<Board> findAll(Pageable pageable);

	//검색
	Page<Board> findByTitleContaining(String title, Pageable pageable);
	Page<Board> findByContentContaining(String content, Pageable pageable);
	Page<Board> findByUserIdContaining(String userId, Pageable pageable);
	
	//추천수
	@Modifying
	@Query("UPDATE Board b SET b.likeCount = b.likeCount + 1 WHERE b.id = :id")
	int incrementLikeCount(@Param("id") Integer id);

	// 예원추가. 마이페이지에 내활동관리에서 게시글 불러올거임
	List<Board> findByUserIdOrderByCreatedAtDesc(String userId);
}
