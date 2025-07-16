package com.example.demo.service;

import com.example.demo.model.entity.Jaso;
import java.util.List;

public interface JasoService {

    Jaso saveJaso(Jaso jaso); // 자소서 저장
    List<Jaso> getJasoListByUserId(String userId); // 사용자 자소서 목록
    List<Jaso> getJasoByUserId(String userId);
    Jaso getJasoById(Long id); // 단일 자소서 조회
    void deleteJaso(Long id); // 자소서 삭제
    
    // 예원추가 >> fragment 처리시 필요. UserController - ("/mypage") 내 jaso 코드 추가
//	List<Jaso> findByUserId(String userId);
	
}