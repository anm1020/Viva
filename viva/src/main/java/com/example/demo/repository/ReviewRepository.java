package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Review;
import com.example.demo.model.entity.Users;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
    List<Review> findByIntrIdOrderByCreatedDtDesc(Integer intrId);
    
    
    // 예원
//    long countByUsers(Users users); // FK 연관관계라면
    // 또는 long countByUserId(String userId);
}