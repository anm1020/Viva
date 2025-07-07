package com.example.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Users;
import com.example.demo.repository.UsersRepository; // 네가 실제 사용하는 레포지토리 import

import lombok.RequiredArgsConstructor;

/**
 * Spring Security에서 사용자 인증 시 호출되는 서비스
 * - userId(아이디)로 DB에서 회원 정보 조회
 * - 찾은 회원 정보를 CustomUserDetails로 감싸서 반환
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Users 테이블에 접근하는 JPA 레포지토리
    private final UsersRepository usersRepository;

    /**
     * 실제 인증 처리 메서드
     * - 로그인 시 userId로 회원을 조회하고
     * - 없으면 예외(UsernameNotFoundException) 발생
     * - 있으면 CustomUserDetails에 담아서 리턴 
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users user = usersRepository.findByUserId(userId);
        if (user == null) throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId);
        return new CustomUserDetails(user);
    }
}
