package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.Notice;
import com.example.demo.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

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
}
