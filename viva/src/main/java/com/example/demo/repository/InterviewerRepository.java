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
		      i.intr_price AS intrPrice
		    FROM interviewer i
		    JOIN users u ON i.user_id = u.user_id
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
	            u.user_career AS userCareer
	        FROM interviewer i
	        JOIN users u ON i.user_id = u.user_id
	        WHERE i.intr_id = :intrId
	        """, nativeQuery = true)
	    Optional<InterviewerDetailDTO> findDetailByIntrId(@Param("intrId") Long intrId);
	}

