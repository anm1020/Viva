package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
	// 페이지처리
	Page<Board> findAll(Pageable pageable);

}
