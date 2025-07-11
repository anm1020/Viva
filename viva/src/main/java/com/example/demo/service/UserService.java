package com.example.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Users;
import com.example.demo.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UsersRepository usersRepository;

	public Users save(Users users) {
		return usersRepository.save(users);
	}

	public boolean isUserIdDuplicate(String userId) {
	    return usersRepository.existsById(userId); // true면 중복!
	}
	
	// "중복방지 회원가입"이 필요하면 아래처럼 추가로 활용 가능
	public void registerUser(Users users) {
	    // 1. user_id 중복 검사
	    if (usersRepository.existsById(users.getUserId())) {
	        // 이미 존재하면 절대로 save() 하지 않는다!
	        throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
	    }
	    // 2. 중복 없으면 저장(이때만 save)
	    usersRepository.save(users);
	}

	public Optional<Users> findByUserId(String userId) {
	    return usersRepository.findByUserId(userId);
	}

	public void deleteUserById(String userId) {
		
	}

	// 관리자모드 회원 계정 상태 변경시 필요 코드
	@Transactional
	public void updateUserType(String userId, String userType) {
	    Users user = usersRepository.findByUserId(userId)
	        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	    
	    user.setUserType(userType);
	    usersRepository.save(user);
	}
	
	// 관리자모드 면접관 권한 부여
	@Transactional
	public void updateUserOuth(String userId, String outhValue) {
	    Users user = usersRepository.findByUserId(userId)
	        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
	    if(!"intr".equals(user.getUserRole()))
	        throw new IllegalArgumentException("면접관만 권한 변경 가능합니다.");
	    user.setUserOuth(outhValue); // 'Y' 또는 'N'
	    usersRepository.save(user);
	}
	
	// 관리자) 회원목록 페이징처리
	public Page<Users> getUserList(Pageable pageable) {
	    return usersRepository.findAll(pageable);
	}
	
	// 상세 정보 조회 (원하면 사용)
//    public User getUserByUserId(String userId) {
//        return userRepository.findByUserId(userId);
//    }


}
