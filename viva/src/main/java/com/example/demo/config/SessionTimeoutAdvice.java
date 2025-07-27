package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SessionTimeoutAdvice {

	// application.properties에 설정한 세션 타임아웃 값(초 단위, 예: 1800)
    @Value("${server.servlet.session.timeout:1800}")
    private String sessionTimeout;

    @ModelAttribute("sessionTimeout")
    public int sessionTimeout() {
        // 초 → 분으로 변환 (예: 1800초 → 30분)
        try {
            return Integer.parseInt(sessionTimeout) / 60;
        } catch (Exception e) {
            return 30; // 기본값(분)
        }
    }
}
