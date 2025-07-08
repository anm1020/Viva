package com.example.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	// 커스텀한 사용자 인증 서비스(로그인 처리에 사용, UserDetailsService 구현)
    private final CustomUserDetailsService userDetailsService; // 커스텀 서비스 주입

    // 1️⃣ 여기 추가! (내부 익명 클래스 형태)
    /**
     * 로그인 성공 시 추가 동작을 정의하는 핸들러(익명 내부 클래스)
     * - 로그인 폼에서 전달받은 'role' 파라미터와
     *   실제 DB에 저장된 회원의 role이 일치하는지 검증
     * - 불일치 시 즉시 세션 무효화(로그아웃) + 로그인 페이지로 에러 파라미터 전달
     * - 일치 시 메인 페이지로 이동(파라미터로 로그인 성공 표시)
     */
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication)
                    throws IOException, ServletException {
            	 System.out.println(">>> [DEBUG] onAuthenticationSuccess principal class: " + authentication.getPrincipal().getClass().getName());
                // 로그인 폼에서 넘어온 role 값(취준생/면접관 구분)
                String formRole = request.getParameter("role"); // "mem" 또는 "intr"
                // 실제 로그인된 사용자의 role
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                String userRole = userDetails.getUsers().getUserRole(); // "mem" 또는 "intr"
                // 다르면 즉시 로그아웃 및 에러 페이지 이동 + alert 에러 메세지
                if (!formRole.equals(userRole)) {
                    request.getSession().invalidate();
                    response.sendRedirect("/loginmain?roleError=true");
                    return;
                }
                // 로그인 성공 시: 메인 페이지로 이동, alert 표시용 파라미터 추가
                response.sendRedirect("/main?loginSuccess=true");
            }
        };
    }
    /** 전체 시큐리티 필터 설정 **/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	// CSRF(크로스 사이트 요청 위조) 보호 비활성화 (개발환경에서만)
            .csrf(csrf -> csrf.disable()) // 개발 시엔 꺼두는 것 OK!
            .authorizeHttpRequests(auth -> auth			// 모든 사용자(비회원 포함) 접근 가능한 페이지
                .requestMatchers(
                		"/loginmain", 			// 로그인 페이지
                		"/memberform",			// 회원가입 페이지
                		"/memberinsert",		// 회원가입 페이지
                		"/css/**", 				// css 파일
                		"/images/**",			// 이미지 파일
                		"/main",				// 메인 페이지
                		"/index.html"
                		).permitAll()	
                .anyRequest().authenticated() 	// 그 외에는 로그인 필요
            )
            // 폼 로그인(아이디/비번 입력 방식) 세부 설정
            .formLogin(form -> form
                .loginPage("/loginmain")          		 // 로그인 페이지
                .loginProcessingUrl("/loginAll")  		 // 로그인 처리 요청 URL (폼의 action과 맞춤)
                .usernameParameter("userId")      		 // 아이디 input 태그 name
                .passwordParameter("password")    	 	 // 비밀번호 input 태그 name
                .successHandler(customSuccessHandler())  // 로그인 성공 시 실행할 핸들러 지정(위에서 정의)
                .failureUrl("/loginmain?error=true")	 // 로그인 실패 시 이동할 URL (파라미터로 실패 alert)
                .permitAll()							 // 로그인 폼 관련 요청 모두 허용
                
            )
            // 소셜 로그인
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/loginmain")		   // 기존 로그인 페이지 그대로
//                .defaultSuccessUrl("/main",true)        // 로그인 성공 시 이동(true설정 : 강제 이동)
                .defaultSuccessUrl("/main?loginSuccess=true", true)
            )
            
            // 로그아웃 처리 세부 설정
            .logout(logout -> logout
                .logoutUrl("/logout")							 // 로그아웃 처리 URL
                .logoutSuccessUrl("/main?logoutSuccess=true")	 // 로그아웃 성공 후 이동할 페이지(메인, 파라미터로 alert 표시)
                .permitAll()									 // 로그아웃 요청 모두 허용
            );
        return http.build();
    }
    /** 비밀번호 인코더(암호화) 빈
     * - 현재는 NoOp(암호화 안 함)
     * - 실서비스 운영/배포 땐 반드시 BCryptPasswordEncoder 등 강력한 암호화로 변경 필요 **/
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
//    	return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();	
    }
    
    
}
