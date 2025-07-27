package com.example.demo.controller;
import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.NoticeComment;
import com.example.demo.service.NoticeCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice/comment")
public class NoticeCommentController {

    private final NoticeCommentService noticeCommentService;

    // 댓글 목록
    @GetMapping("/{noticeId}")
    public List<NoticeComment> getComments(@PathVariable("noticeId") Long noticeId) {
        return noticeCommentService.getCommentsByNoticeId(noticeId);
    }

    // 댓글 등록
    @PostMapping("/add")
    public NoticeComment addComment(
            @RequestParam("noticeId") Long noticeId,
            @RequestParam("content") String content,
            Principal principal) {
        String userId = principal.getName();
        return noticeCommentService.addComment(noticeId, userId, content);
    }

    // 댓글 삭제
    @PostMapping("/delete")
    public String deleteComment(
            @RequestParam("commentId") Long commentId,
            Principal principal) {
        String userId = principal.getName();
        noticeCommentService.deleteComment(commentId, userId);
        return "ok";
    }
}
