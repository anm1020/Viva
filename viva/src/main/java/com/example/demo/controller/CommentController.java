package com.example.demo.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.entity.Board;
import com.example.demo.model.entity.Comment;
import com.example.demo.model.entity.Users;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    // 🔹 댓글 저장
    @PostMapping("/add")
    public String addComment(@RequestParam("boardId") Integer boardId,
                             @RequestParam("content") String content,
                             HttpSession session) {

        Users loginUser = (Users) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login"; // 로그인 안 되어 있으면 로그인 페이지로
        }

        Board board = boardService.getBoardById(boardId);
        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setUser(loginUser);
        comment.setContent(content);

        commentService.saveComment(comment);
        return "redirect:/board/view/" + boardId;
    }

    // 🔹 댓글 삭제
    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long commentId,
                                @RequestParam("boardId") Integer boardId,
                                HttpSession session) {

        Users loginUser = (Users) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        // (선택) 권한 체크: 본인만 삭제 가능 등

        commentService.deleteComment(commentId);
        return "redirect:/board/view/" + boardId;
    }

    // 🔹 댓글 수정 처리
    @PostMapping("/update")
    public String updateComment(@RequestParam("commentId") Long commentId,
                                @RequestParam("boardId") Integer boardId,
                                @RequestParam("content") String content,
                                HttpSession session) {

        Users loginUser = (Users) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login";
        }

        // (선택) 권한 체크: 본인만 수정 가능 등

        commentService.updateComment(commentId, content);
        return "redirect:/board/view/" + boardId;
    }
}
