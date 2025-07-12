package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.Board;
import com.example.demo.model.entity.Comment;
import com.example.demo.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 🔹 특정 게시글의 댓글 목록 (최신순)
    public List<Comment> getCommentsByBoard(Board board) {
        return commentRepository.findByBoardOrderByCreatedAtDesc(board);
    }

    // 🔹 댓글 저장
    @Transactional
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    // 🔹 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // 🔹 댓글 수정
    @Transactional
    public void updateComment(Long commentId, String newContent) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(newContent);              // 내용 수정
            comment.setUpdatedAt(java.time.LocalDateTime.now()); // 수정일 갱신 (수동으로)
            commentRepository.save(comment);             // 저장
        } else {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId);
        }
    }
}
