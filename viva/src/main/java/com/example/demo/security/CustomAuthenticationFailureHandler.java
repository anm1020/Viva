package com.example.demo.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.AuthenticationException exception)
                                        throws IOException, ServletException {

        String errorMsg = "error";
        if(exception instanceof UsernameNotFoundException) {
            errorMsg = "noid"; // 아이디 없음
        } else if(exception instanceof BadCredentialsException) {
            errorMsg = "bad";
        } // else 기타 예외 처리 가능

        response.sendRedirect("/loginmain?error=" + errorMsg);
    }
}
