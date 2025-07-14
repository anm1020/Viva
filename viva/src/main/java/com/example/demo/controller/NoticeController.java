package com.example.demo.controller;

import com.example.demo.model.entity.Notice;
import com.example.demo.model.entity.Users;
import com.example.demo.service.NoticeService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;

    // 1. 공지사항 목록 보기 (전체)
    @GetMapping("/list")
    public String noticeList(Model model, Principal principal) {
        List<Notice> noticeList = noticeService.getNoticeList();
        model.addAttribute("noticeList", noticeList);

        // 관리자 여부 판별
        boolean isAdmin = false;
        if (principal != null) {
            String userId = principal.getName();
            isAdmin = "admin".equals(userId);
        }
        model.addAttribute("isAdmin", isAdmin);

        return "notice/list";
    }

    // 2. 공지사항 작성 (POST)
    @PostMapping("/new")
    public String createNotice(@ModelAttribute Notice notice, Principal principal, Model model) {
        String userId = principal.getName();
        Users users = userService.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));
        notice.setUser(users);
        noticeService.save(notice);

        // 작성 후 목록 리다이렉트
        model.addAttribute("isAdmin", true);
        model.addAttribute("msg", "등록되었습니다!");
        return "redirect:/notice/list";
    }

    // 3. 글쓰기 폼 띄우기 (GET)
    @GetMapping("/new")
    public String showNoticeForm(Model model, Principal principal) {
        String userId = principal.getName();
        if (!"admin".equals(userId)) {
            return "error/403";
        }
        model.addAttribute("notice", new Notice());
        return "notice/form";
    }

    // 4. 수정 폼 보여주기 (GET)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, Principal principal) {
        Notice notice = noticeService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));

        String userId = principal.getName();
        Users loginUser = userService.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));

        boolean isAdmin = "admin".equals(loginUser.getUserId());
        boolean isOwner = notice.getUser().getUserId().equals(loginUser.getUserId());
        if (!(isAdmin || isOwner)) {
            return "error/403";
        }

        model.addAttribute("notice", notice);
        return "notice/edit";
    }

    // 5. 공지사항 수정 (POST)
    @PostMapping("/edit/{id}")
    public String updateNotice(@PathVariable("id") Long id,
                               @ModelAttribute Notice notice,
                               Principal principal,
                               RedirectAttributes rttr) {
        String userId = principal.getName();
        Users loginUser = userService.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));

        Notice oldNotice = noticeService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));

        boolean isAdmin = "admin".equals(loginUser.getUserId());
        boolean isOwner = oldNotice.getUser().getUserId().equals(loginUser.getUserId());
        if (!(isAdmin || isOwner)) {
            return "error/403";
        }

        noticeService.updateNotice(id, notice);

        rttr.addFlashAttribute("msg", "수정되었습니다!");
        return "redirect:/notice/list";
    }

    // 6. 공지 상세 보기
    @GetMapping("/{id}")
    public String noticeDetail(@PathVariable("id") Long id, Model model) {
        Notice notice = noticeService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
        model.addAttribute("notice", notice);
        return "notice/detail";
    }

    // 7. 공지사항 삭제
    @PostMapping("/delete/{id}")
    @ResponseBody
    public void deleteNotice(@PathVariable("id") Long id) {
        noticeService.deleteNotice(id);
    }
}
