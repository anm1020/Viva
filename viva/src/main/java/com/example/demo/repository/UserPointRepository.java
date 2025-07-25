package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entity.UserPoint;

import jakarta.transaction.Transactional;

public interface UserPointRepository extends JpaRepository<UserPoint, String> {
	 // 사용자 현재 포인트 조회 (기본 제공 findById 사용해도 됨)
	  Optional<UserPoint> findByUserId(String userId);
	  
	// 사용자 포인트 차감 (슬님띠)
//	    @Modifying
//	    @Transactional
//	    @Query("UPDATE UserPoint up SET up.point = up.point - :amount WHERE up.userId = :userId AND up.point >= :amount")
//	    int deductPoint(@Param("userId") String userId, @Param("amount") int amount);

		// 사용자 현재 포인트 조회 (예원)
    @Query("SELECT up.point FROM UserPoint up WHERE up.userId = :userId")
    int findPointByUserId(@Param("userId") String userId);
    
    
    /** 예원
     * 포인트 차감: 성공 시 1 리턴, 실패(충분치 않을 때) 시 0 리턴
     */
    @Modifying
    @Transactional
    @Query("""
        UPDATE UserPoint up
        SET up.point = up.point - :amount
        WHERE up.userId = :userId
          AND up.point >= :amount
        """)
    int deductPoint(@Param("userId") String userId,
                    @Param("amount") int amount);
    
    
    /**
     * (선택) 포인트 환급: 거절 시 사용
     */
    @Modifying
    @Transactional
    @Query("""
        UPDATE UserPoint up
        SET up.point = up.point + :amount
        WHERE up.userId = :userId
        """)
    int addPoint(@Param("userId") String userId,
                 @Param("amount") int amount);
    
}
