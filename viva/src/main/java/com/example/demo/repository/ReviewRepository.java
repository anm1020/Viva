package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entity.Review;
import com.example.demo.model.entity.Users;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
    List<Review> findByIntrIdOrderByCreatedDtDesc(Integer intrId);
    
    
    // 예원
//    long countByUsers(Users users); // FK 연관관계라면
    // 또는 long countByUserId(String userId);
    
    // 예원추가 : 마페 내활동관리에 리뷰 띄우기ㅠㅠ 면접관 PK로 리뷰 목록 가져오기
    // 🔹 Interviewer.sirNo 기준으로 정렬된 리뷰 리스트 반환
    List<Review> findByUserIdOrderByCreatedDtDesc(String userId);

    // 면접관 마페에서 리뷰 확인
    @Query("SELECT r FROM Review r WHERE r.interviewer.userId = :userId")
    List<Review> getReviewsByInterviewerId(@Param("userId") String userId);
}