package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiRequestDTO {
    private String sessionId;
    private String prompt;  // 사용자 질문
}
