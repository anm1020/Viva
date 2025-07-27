package com.example.demo.repository;

import com.example.demo.model.entity.Reviewboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewboardRepository extends JpaRepository<Reviewboard, Long> {
    // 기본 CRUD와 페이징/정렬 지원
	   List<Reviewboard> findByUser_UserId(String userId);
}
