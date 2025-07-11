package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.dto.InterviewerDTO;
import com.example.demo.model.dto.InterviewerDetailDTO;
import com.example.demo.model.entity.Interviewer;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {

	@Query(value = """
		    SELECT 
		      i.intr_id AS intrId,
		      i.user_id AS userId,
		      u.user_name AS userName,
		      i.intr_image AS intrImage,
		      u.user_skill AS userSkill,
		      i.intr_cate AS intrCate,
		      i.intr_price AS intrPrice,
		      i.intr_intro AS intrIntro,
		      COALESCE(AVG(r.score), 0) AS avgScore,
		      COUNT(r.review_no) AS reviewCount
		    FROM interviewer i
		    JOIN users u ON i.user_id = u.user_id
		    LEFT JOIN reviews r ON i.intr_id = r.intr_id
		    GROUP BY i.intr_id, i.user_id, u.user_name, i.intr_image, u.user_skill, i.intr_cate, i.intr_price, i.intr_intro
		    ORDER BY i.intr_created_at DESC
		""", nativeQuery = true)
		List<InterviewerDTO> findAllForList();
	

	@Query(value = """
	        SELECT 
	            i.intr_id AS intrId,
	            u.user_id AS userId,
	            u.user_name AS userName,
	            i.intr_image AS intrImage,
	            i.intr_price AS intrPrice,
	            u.user_skill AS userSkill,
	            i.intr_cate AS intrCate,
	            i.intr_content AS intrContent, 
	            u.user_career AS userCareer,
	            i.intr_intro AS intrIntro
	        FROM interviewer i
	        JOIN users u ON i.user_id = u.user_id
	        WHERE i.intr_id = :intrId
	        """, nativeQuery = true)
	    Optional<InterviewerDetailDTO> findDetailByIntrId(@Param("intrId") Long intrId);
	    
	@Query(value = """
		    SELECT 
		      i.intr_id AS intrId,
		      i.user_id AS userId,
		      u.user_name AS userName,
		      i.intr_image AS intrImage,
		      u.user_skill AS userSkill,
		      i.intr_cate AS intrCate,
		      i.intr_price AS intrPrice,
		      i.intr_intro AS intrIntro,
		      COALESCE(AVG(r.score), 0) AS avgScore,
		      COUNT(r.review_no) AS reviewCount
		    FROM interviewer i
		    JOIN users u ON i.user_id = u.user_id
		    LEFT JOIN reviews r ON i.intr_id = r.intr_id
		    WHERE i.intr_cate LIKE %:category%
		    GROUP BY i.intr_id, i.user_id, u.user_name, i.intr_image, u.user_skill, i.intr_cate, i.intr_price, i.intr_intro
		    ORDER BY i.intr_created_at DESC
		""", nativeQuery = true)
		List<InterviewerDTO> findByCategory(@Param("category") String category);
	}

