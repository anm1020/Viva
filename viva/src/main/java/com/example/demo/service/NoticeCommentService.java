package com.example.demo.service;

import com.example.demo.model.entity.NoticeComment;
import com.example.demo.model.entity.Notice;
import com.example.demo.model.entity.Users;
import com.example.demo.repository.NoticeCommentRepository;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeCommentService {

    private final NoticeCommentRepository noticeCommentRepository;
    private final NoticeRepository noticeRepository;
    private final UsersRepository userRepository;

    // 댓글 목록
    public List<NoticeComment> getCommentsByNoticeId(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow();
        return noticeCommentRepository.findByNoticeOrderByCreatedAtAsc(notice);
    }

    // 댓글 등록
    public NoticeComment addComment(Long noticeId, String userId, String content) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow();
        Users user = userRepository.findById(userId).orElseThrow();
        NoticeComment comment = NoticeComment.builder()
                .notice(notice)
                .user(user)
                .content(content)
                .build();
        return noticeCommentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, String userId) {
        NoticeComment comment = noticeCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));
        // 본인/관리자만 삭제 (추가 구현)
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("본인만 삭제할 수 있습니다.");
        }
        noticeCommentRepository.deleteById(commentId);
    }
}
