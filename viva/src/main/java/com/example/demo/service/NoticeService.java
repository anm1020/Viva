package com.example.demo.service;

import com.example.demo.model.entity.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeService {
	
	
	// 대시보드에서 공지 등록)
    Notice save(Notice notice);
    List<Notice> getNoticeList();
    
    // 추가 (대시보드에서 공지 수정)
    Optional<Notice> findById(Long id);
    Notice updateNotice(Long id, Notice notice);
    
    void deleteNotice(Long id);
    
    
}