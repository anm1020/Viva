package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.entity.Users;

public interface TestUserRepository extends JpaRepository<Users, String> {

	/**
	 * user_role 컬럼이 'intr' 인 행만 네이티브 SQL로 가져오기 이 방식은 Users 엔티티에 userRole 필드가 없어도,
	 * 
	 * DB 테이블의 user_role 컬럼에서 직접 intr만 걸러와 줍니다.
	 */
	@Query(value = "SELECT * FROM users WHERE user_role = 'intr'", nativeQuery = true)
	List<Users> findInterviewersNative();

	/**
	 * user_name 에 키워드 포함 검색 예시
	 */
	List<Users> findByUserNameContaining(String keyword);

	/**
	 * 단일 조회용 메서드 (Optional 대신 바로 엔티티 반환)
	 */
	Users getByUserId(String userId);
	
	Optional<Users> findByUserId(String userId); // userId는 고유 아이디 (ex. 이메일 또는 아이디)
	
}