package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.entity.Users;

public interface UsersRepository extends JpaRepository<Users, String>{

//	Users findByUserId(String userId); // Optional 없이!
	
	// 전체 조회는 기본 findAll() 사용 가능
    // 검색 쿼리는 이렇게 추가
    List<Users> findByUserNameContainingOrUserIdContaining(String name, String id);

    // 대시보드 회원 목록 뿌려주기. 가입일 기준 최신순 정렬
	List<Users> findAllByOrderByCreatedDtDesc();

	List<Users> findByUserRole(String userRole);
	
	Optional<Users> findByUserId(String userId);
	
	Page<Users> findAllByUserRole(String userRole, Pageable pageable);
	
	

//	List<Users> findByUserIdContainingOrUserNameContainingOrderByCreatedDtDesc(String keyword, String keyword2);

//    List<Users> findByUserIdContainingOrUserNameContainingOrderByCreatedDtDesc(Integer userId, String userName);

}
