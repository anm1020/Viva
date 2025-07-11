package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Board;
import com.example.demo.model.entity.Users;

public interface BoardRepository extends JpaRepository<Board, Integer> {
//	    long countByUsers(Users users); // FK 연관관계라면
	    // 또는 long countByUserId(String userId);

}
