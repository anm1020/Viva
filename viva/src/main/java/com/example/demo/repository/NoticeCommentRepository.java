package com.example.demo.repository;

import com.example.demo.model.entity.NoticeComment;
import com.example.demo.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
    List<NoticeComment> findByNoticeOrderByCreatedAtAsc(Notice notice);
    // 또는: List<NoticeComment> findByNotice_NoticeIdOrderByCreatedAtAsc(Long noticeId);
}
