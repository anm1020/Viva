package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Notice;
import com.example.demo.repository.NoticeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

//    @Override
//    public List<Notice> getNoticeList() {
//        // 전체 공지 목록 조회 (필요 시 정렬 추가)
//        return noticeRepository.findAll();
//    }
//
//    @Override
//    public Notice getNotice(Long id) {
//        // 단일 공지 조회, 없으면 예외
//        return noticeRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다. id=" + id));
//    }
//
//    @Override
//    public Notice saveNotice(Notice notice) {
//        // 신규 공지 저장
//        return noticeRepository.save(notice);
//    }
//
//    @Override
//    public Notice updateNotice(Long id, Notice notice) {
//        // 기존 공지 가져와서 제목·내용만 업데이트 후 저장
//        Notice existing = noticeRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다. id=" + id));
//        existing.setTitle(notice.getTitle());
//        existing.setContent(notice.getContent());
//        // 필요하다면 updatedDate 등도 설정
//        return noticeRepository.save(existing);
//    }
//
//    @Override
//    public void deleteNotice(Long id) {
//        // 공지 삭제
//        noticeRepository.deleteById(id);
//    }
//}

    
    
    /**
     * 공지사항 저장
     */
//    @Override
//    public Notice save(Notice notice) {
//        return noticeRepository.save(notice);
//    }
//
//    /**
//     * 전체 공지를 생성일 내림차순으로 조회
//     */
//    @Override
//    public List<Notice> getNoticeList() {
//        return noticeRepository.findAllByOrderByCreatedAtDesc();
//    }
//
//    /**
//     * 단일 공지 조회 (Optional로 래핑)
//     */
//    @Override
//    public Optional<Notice> findById(Long id) {
//        return noticeRepository.findById(id);
//    }
//
//    /**
//     * 조회수 1 증가 (트랜잭션 내에서 Dirty Checking)
//     */
//    @Override
//    @Transactional
//    public void incrementViewCount(Long noticeId) {
//        Notice n = noticeRepository.findById(noticeId)
//            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
//        n.setViewCount(n.getViewCount() + 1);
//        // 변경된 엔티티는 트랜잭션 커밋 시 자동 반영됩니다.
//    }

    /**
     * 공지사항 수정:
     * - 기존 엔티티를 조회 → 필요한 필드만 덮어쓰기 → 저장
     */
//    @Override
//    @Transactional
//    public Notice updateNotice(Long id, Notice notice) {
//        Notice existing = noticeRepository.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
//        existing.setTitle(notice.getTitle());
//        existing.setContent(notice.getContent());
//        existing.setIsFixed(notice.getIsFixed());
//        // 필요 시 updatedAt은 @EntityListeners로 자동 업데이트 설정하거나
//        // 직접 existing.setUpdatedAt(LocalDateTime.now()) 해주셔도 됩니다.
//        return noticeRepository.save(existing);
//    }
//
//    /**
//     * 공지사항 삭제
//     */
//    @Override
//    @Transactional
//    public void deleteNotice(Long id) {
//        // 존재 여부 확인 후 삭제
//        if (!noticeRepository.existsById(id)) {
//            throw new IllegalArgumentException("삭제할 공지사항이 없습니다.");
//        }
//        noticeRepository.deleteById(id);
//    }
//
//    /**
//     * 최신 공지 3개 조회
//     */
//    @Override
//    public List<Notice> getLatestNotices() {
//        return noticeRepository.findTop3ByOrderByCreatedAtDesc();
//    }
    
    
    
    // 게시판에 공지
    private final NoticeRepository noticeRepo;

    @Override
    public Notice save(Notice notice) {
        return noticeRepository.save(notice); 
    }

    @Override
    public List<Notice> getNoticeList() {
        return noticeRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Optional<Notice> findById(Long id) {
        return noticeRepository.findById(id);
    }
    
    // 공지 조회수 증가
    @Override
    @Transactional
    public void incrementViewCount(Long noticeId) {
        Notice n = noticeRepo.findById(noticeId)
            .orElseThrow(() -> new IllegalArgumentException("공지 없음"));
        n.setViewCount(n.getViewCount() + 1);
        // 영속성 컨텍스트 변경만으로 save 없이 반영됩니다.
    }

    @Override
    @Transactional
    public Notice updateNotice(Long id, Notice notice) {
        Notice existingNotice = noticeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));

        // 필요한 필드만 변경
        existingNotice.setTitle(notice.getTitle());
        existingNotice.setContent(notice.getContent());
        existingNotice.setIsFixed(notice.getIsFixed());
        // 기타 필드 필요하면 추가

        // JPA dirty checking, save는 옵션
        return noticeRepository.save(existingNotice);
    }
    
    @Override
    @Transactional
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
    
//    @Override
//    public List<Notice> getNoticeList() {
//        return noticeRepository.findAllByOrderByCreatedAtDesc();
//    }
    
    // 게시판에 공지 연결 (Y로 설정된&최신순 3개)
    @Override
    public List<Notice> getLatestNotices() {
        return noticeRepository.findTop3ByIsFixedOrderByCreatedAtDesc("Y");
    }
}