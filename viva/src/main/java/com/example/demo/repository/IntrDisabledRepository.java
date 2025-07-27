package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.IntrDisabled;

public interface IntrDisabledRepository extends JpaRepository<IntrDisabled, Long> {

    // 특정 면접관의 비활성화된 날짜/시간 전체 조회
    List<IntrDisabled> findByIntrId(String intrId);

    // 특정 날짜+시간이 비활성화되었는지 조회
    boolean existsByIntrIdAndDisabledDateAndDisabledTime(String intrId, String disabledDate, String disabledTime);
    
    // id(PK) 기준 삭제 메서드
    void deleteById(Long id);
    
    
}
