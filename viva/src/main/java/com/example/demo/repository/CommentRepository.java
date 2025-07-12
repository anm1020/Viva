package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Board;
import com.example.demo.model.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

	// 특정 게시글의 댓글 목록 조회 (최신순)
	List<Comment> findByBoardOrderByCreatedAtDesc(Board board);
}
