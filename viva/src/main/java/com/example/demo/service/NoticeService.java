package com.example.demo.service;

import com.example.demo.model.entity.Notice;

import java.util.List;
import java.util.Optional;
// 공지사항 관련 비즈니스 로직을 정의하는 Service 인터페이스
public interface NoticeService {
	
	
//	List<Notice> getNoticeList();
//	Notice getNotice(Long id);
//	Notice saveNotice(Notice notice);
//	Notice updateNotice(Long id, Notice notice);
//	void deleteNotice(Long id);
	
	
//	/**
//     * 공지사항 저장
//     * @param notice 새로 작성한 Notice 엔티티
//     * @return 저장된 Notice (ID, 생성일 등이 채워진 상태)
//     */
//     Notice save(Notice notice);
//
//    /**
//     * 전체 공지사항 목록을 최신순으로 반환
//     * @return Notice 리스트
//     */
//    List<Notice> getNoticeList();
//
//    /**
//     * ID로 공지사항 조회
//     * @param id 공지사항 PK
//     * @return Optional<Notice>
//     */
//    Optional<Notice> findById(Long id);
//
//    /**
//     * 조회수 1 증가
//     * @param noticeId 조회할 공지사항 PK
//     */
//    void incrementViewCount(Long noticeId);
//
//    /**
//     * 공지사항 수정
//     * @param id    수정할 공지사항 PK
//     * @param notice 수정폼에서 전달된 Notice 객체 (title, content 등)
//     * @return 수정된 Notice
//     */
//     Notice updateNotice(Long id, Notice notice);
//
//    /**
//     * 공지사항 삭제
//     * @param id 삭제할 공지사항 PK
//     */
//    void deleteNotice(Long id);
//
//    /**
//     * 최신 공지 3개만 반환 (메인 대시보드 등에서 사용)
//     * @return 최신 Notice 3개
//     */
//     List<Notice> getLatestNotices();
	
	/*
	 * // 공지 조회(상세) Optional<Notice> findById(Long id);
	 * 
	 * // 공지 수정 Notice updateNotice(Long id, Notice notice);
	 * 
	 * // 공지 삭제 void deleteNotice(Long id);
	 * 
	 * // 게시판에 공지 3개 띄우기 List<Notice> getLatestNotices();
	 */
	
	
	
	// 대시보드에서 공지 등록)
    Notice save(Notice notice);
    List<Notice> getNoticeList();
    
    // 공지 조회수 증가
    void incrementViewCount(Long noticeId);
    
    // 공지 조회(상세) > 일반 게시판
    Optional<Notice> findById(Long id);
    // 추가 (대시보드에서 공지 수정)
    Notice updateNotice(Long id, Notice notice);
    
    // 대시보드에서 공지 삭제
    void deleteNotice(Long id);
    
    // 게시판에 공지 3개 띄우기
    List<Notice> getLatestNotices();
    
	static Notice getNotice(Long id) {
		return null;
	}
	
	
   
    
    
}