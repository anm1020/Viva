package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}

	public boolean isUserIdDuplicate(String userId) {
	    return userRepository.existsById(userId); // true면 중복!
	}
	
	// "중복방지 회원가입"이 필요하면 아래처럼 추가로 활용 가능
//	public void registerUser(User user) {
//	    if (isUserIdDuplicate(user.getUserId())) {
//	        throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
//	    }
//	    userRepository.save(user);
	
	
	public void registerUser(User user) {
	    // 1. user_id 중복 검사
	    if (userRepository.existsById(user.getUserId())) {
	        // 이미 존재하면 절대로 save() 하지 않는다!
	        throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
	    }
	    // 2. 중복 없으면 저장(이때만 save)
	    userRepository.save(user);
	}

	public User login(String userId) {
		return userRepository.findByUserId(userId);
	}

	// 상세 정보 조회 (원하면 사용)
//    public User getUserByUserId(String userId) {
//        return userRepository.findByUserId(userId);
//    }


}
