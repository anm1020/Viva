package com.example.demo.security;

import com.example.demo.model.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security에서 인증 처리 시 사용하는 사용자 정보 구현체
 * Users 엔티티를 래핑해서 시큐리티가 사용할 수 있게 변환
 */
public class CustomUserDetails implements UserDetails {

    // 실제 사용자 정보를 담고 있는 Users 엔티티
    private final Users users;

    // 생성자: 인증 대상 사용자를 받아서 저장
    public CustomUserDetails(Users users) {
        this.users = users;
    }

    /**
     * 사용자의 권한 정보를 반환
     * - userRole이 "mem", "intr" 등으로 들어올 때
     * - Spring Security는 권한 명칭 앞에 "ROLE_"이 붙는 것을 권장함
     * - ex) userRole = "mem"이면 ROLE_MEM, "intr"이면 ROLE_INTR
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (users.getUserRole() != null) {
            return Collections.singleton(() -> "ROLE_" + users.getUserRole().toUpperCase());
        }
        return Collections.emptyList(); // 권한이 없을 경우 빈 리스트 반환
    }

    /**
     * 사용자의 비밀번호 반환
     * - 시큐리티가 내부적으로 로그인 검증에 사용
     */
    @Override
    public String getPassword() {
        return users.getUserPass();
    }

    /**
     * 사용자의 아이디 반환
     * - 시큐리티가 내부적으로 로그인 검증에 사용
     */
    @Override
    public String getUsername() {
        return users.getUserId();
    }

    /**
     * 계정 만료 여부
     * - true면 만료 안 됨 (항상 사용 가능)
     */
    @Override
    public boolean isAccountNonExpired() { return true; }

    /**
     * 계정 잠김 여부
     * - true면 잠금 안 됨 (항상 사용 가능)
     */
    @Override
    public boolean isAccountNonLocked() { return true; }

    /**
     * 비밀번호 만료 여부
     * - true면 만료 안 됨 (항상 사용 가능)
     */
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /**
     * 계정 활성화 여부
     * - true면 활성화 (항상 사용 가능)
     */
    @Override
    public boolean isEnabled() { return true; }

    /**
     * Users 엔티티 자체를 반환 (추가 정보가 필요할 때 사용)
     */
    public Users getUsers() {
        return users;
    }
    
    
    // 추가 (마이페이지)
    /**
     * 현재 사용자의 역할(권한) 값("mem", "intr" 등)을 반환
     */
//    public String getRole() {
//        return users.getUserRole();
//    }

    /**
     * 현재 사용자의 이름을 반환
     */
//    public String getDisplayName() {
//        return users.getUserName();
//    }

    /**
     * 현재 사용자의 이메일을 반환
     */
//    public String getEmail() {
//        return users.getUserEmail();
//    }

    
}
