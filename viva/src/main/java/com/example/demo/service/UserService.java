package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Users;
import com.example.demo.repository.UsersRepository;

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
//	public void registerUser(User user) {
//	    if (isUserIdDuplicate(user.getUserId())) {
//	        throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
//	    }
//	    userRepository.save(user);
	
	
	public void registerUser(Users users) {
	    // 1. user_id 중복 검사
	    if (usersRepository.existsById(users.getUserId())) {
	        // 이미 존재하면 절대로 save() 하지 않는다!
	        throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
	    }
	    // 2. 중복 없으면 저장(이때만 save)
	    usersRepository.save(users);
	}

//	public Users login(String userId) {
//		return usersRepository.findByUserId(userId);
//	}

	public Users findByUserId(String userId) {
		return usersRepository.findByUserId(userId);
	}

	// 상세 정보 조회 (원하면 사용)
//    public User getUserByUserId(String userId) {
//        return userRepository.findByUserId(userId);
//    }


}
