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
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        Users users = usersRepository.findByUserId(userId);
//        if (users == null) throw new UsernameNotFoundException("존재하지 않는 계정입니다: " + userId);
//        return new CustomUserDetails(users);
//    }
    
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users users = usersRepository.findByUserId(username);
//        if(user == null) throw new UsernameNotFoundException("존재하지 않는 계정");
//        if("N".equals(user.getUserType())) throw new DisabledException("비활성화 계정입니다.");

        // 여기까지 오면 user_type == 'Y'
        // UserDetails 반환
//    }
    
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users users = usersRepository.findByUserId(userId);
        if(users == null || "N".equals(users.getUserType())) {
            // N이면 "존재하지 않는 아이디"와 똑같이 처리
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        }
        // 정상적으로 UserDetails 생성
     // Spring Security가 요구하는 UserDetails 객체를 반환해야 함
        // 예를 들어, 직접 만든 UsersDetailsImpl(user)이나, 아래처럼 new org.springframework.security.core.userdetails.User 사용 가능
 
        return new CustomUserDetails(users);  // ★ 이거로 변경!
//        		org.springframework.security.core.userdetails.User
//                .withUsername(users.getUserId())
//                .password(users.getUserPass())
//                .roles(users.getUserRole())  // Enum -> String
//                .build();
                
                // user_role이 enum/string이면 .roles() 혹은 .authorities()로 변환
    }
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        Users users = usersRepository.findByUserId(userId);
//        if (users == null || "N".equals(users.getUserType())) {
//            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
//        }
//        return new CustomUserDetails(users); // ← 이 부분 중요!
//    }
    
}
