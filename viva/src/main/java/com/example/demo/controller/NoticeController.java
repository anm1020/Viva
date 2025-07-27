package com.example.demo.controller;

import com.example.demo.model.entity.Notice;
import com.example.demo.model.entity.NoticeComment;
import com.example.demo.model.entity.Users;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.service.NoticeCommentService;
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

	private final NoticeRepository noticeRepository;
    private final NoticeService noticeService;
    private final UserService userService;
    private final NoticeCommentService noticeCommentService;

    // 관리자 여부 헬퍼
    private boolean isAdmin(Principal principal) {
        return principal != null && "admin".equals(principal.getName());
    }

    // 1. 목록
    @GetMapping("/noticeList")
    public String noticeList(Model model) {
        List<Notice> topFixedNotices = noticeRepository.findTop3ByIsFixedOrderByCreatedAtDesc("Y");
        List<Notice> noticeList = noticeRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("topFixedNotices", topFixedNotices);
        model.addAttribute("noticeList", noticeList);
        return "notice/noticeList";
    }

	
    // 공지 글 작성 ㅊㅊ
    @GetMapping("/new")
    public String showNoticeForm(Model model, Principal principal) {
        if (!isAdmin(principal)) return "error/403";
        model.addAttribute("notice", new Notice());
        return "notice/write :: noticeContent";  // ✅ 프래그먼트만 반환
    }
    
    // 글작성시 등록 요청하는 코드 ㅊㅊ
    @PostMapping("/save")
    @ResponseBody
    public String saveNotice(@ModelAttribute Notice notice, Principal principal) {
        String userId = principal.getName();
        Users user = userService.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        notice.setUser(user);
        noticeService.save(notice);
        return "success";
    }
    
    // 상세보기 ㅊㅊ
    // 관리자 대시보드에서 AJAX 요청
    @GetMapping("/detail/{id}")
    public String adminNoticeDetail(@PathVariable("id") Long id, Model model, Principal principal) {
        Notice notice = noticeService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지 없음: " + id));
        noticeService.incrementViewCount(id);
        model.addAttribute("notice", notice);
        model.addAttribute("isAdmin", isAdmin(principal));
        return "notice/detail :: adminContent";
    }

    // 일반 사용자: 전체 HTML 페이지 반환
    @GetMapping("/board/notice/{id}")
    public String userNoticeDetail(@PathVariable("id") Long id, Model model, Principal principal) {
        Notice notice = noticeService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("공지 없음"));
        noticeService.incrementViewCount(id);
        model.addAttribute("notice", notice);
        model.addAttribute("isAdmin", isAdmin(principal)); // ✅ 추가!
        
        // ★ 댓글 리스트 가져오기
        List<NoticeComment> commentList = noticeCommentService.getCommentsByNoticeId(id);
        model.addAttribute("commentList", commentList);
        
        return "notice/noticeDetailPage";
    }
    
    // 공지 수정. 상세페이지에서 바로 수정(따로 폼 필요없음)
 // 수정 (폼 전체를 serialize로 전송)
    @PostMapping("/update")
    @ResponseBody
    public String updateNotice(@ModelAttribute Notice notice, Principal principal) {
        if (!isAdmin(principal)) return "권한없음";
        String userId = principal.getName();
        Users user = userService.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        notice.setUser(user);
        noticeService.updateNotice(notice.getNoticeId(), notice); // noticeId는 notice 객체에서 꺼냄
        return "success";
    }

    // 삭제 (noticeId는 URL에서 받음)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteNotice(@PathVariable("id") Long id, Principal principal) {
        if (!isAdmin(principal)) return "권한없음";
        noticeService.deleteNotice(id);
        return "success";
    }
    
    // 일반 게시판에 공지 추천
    @PostMapping("/like/{id}")
    @ResponseBody
    public String likeNotice(@PathVariable("id") Long id) {
        Notice notice = noticeService.findById(id).orElseThrow();
        notice.setLikeCount(notice.getLikeCount() == null ? 1 : notice.getLikeCount() + 1);
        noticeService.save(notice);
        return "ok";
    }
    
    
    
    
//    // 4. 수정 폼
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable("id") Long id,
//                               Model model,
//                               Principal principal) {
//        Notice notice = noticeService.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
//        Users loginUser = userService.findByUserId(principal.getName())
//            .orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));
//
//        boolean isOwner = notice.getUser().getUserId().equals(loginUser.getUserId());
//        if (!isAdmin(principal) && !isOwner) {
//            return "error/403";
//        }
//        
//        System.out.println(notice);
//        model.addAttribute("notice", notice);
//        return "notice/edit";
//    }
//
//    // 5. 수정 처리
//    @PostMapping("/edit/{id}")
//    public String updateNotice(@PathVariable("id") Long id,
//                               @ModelAttribute Notice notice,
//                               Principal principal,
//                               RedirectAttributes rttr) {
//        Users loginUser = userService.findByUserId(principal.getName())
//            .orElseThrow(() -> new IllegalArgumentException("사용자 정보 없음"));
//        Notice existing = noticeService.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
//
//        boolean isOwner = existing.getUser().getUserId().equals(loginUser.getUserId());
//        if (!isAdmin(principal) && !isOwner) {
//            return "error/403";
//        }
//
//        noticeService.updateNotice(id, notice);
//        rttr.addFlashAttribute("msg", "수정되었습니다!");
//        return "redirect:/notice/noticeList";
//    }

    
 // 유저용 전체 페이지
//    @GetMapping("/{id}")
//    public String userDetail(@PathVariable("id") Long id, Model m) {
//      //m.addAttribute("notice", noticeService.findById(id));
//      
//      Notice notice = noticeService.findById(id)
//              .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
//      m.addAttribute("notice", notice);
//      m.addAttribute("isAdmin", false);
//      
//      System.out.println("m" + m.getAttribute("notice"));
//      
//      
//      return "notice/detail";
//    }
//
//    // 관리자 대시보드 AJAX: fragment만 반환
//    @GetMapping("/notice/{id}")
//    public String adminDetail(@PathVariable Long id, Model m) {
//      m.addAttribute("notice", noticeService.findById(id));
//      m.addAttribute("isAdmin", true);
//      // Thymeleaf에게 fragments/noticeAdmin.html의 adminContent만 렌더링하도록 지시
//      return "fragments/noticeAdmin :: adminContent";
//    }
    
    
//    @GetMapping("/{id}")
//    public String noticeDetail(
//        @PathVariable("id") Long id,
//        Model model,
//        Principal principal
//    ) {
//    	Notice notice = noticeService.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
//        model.addAttribute("notice", notice);
//        model.addAttribute("isAdmin", isAdmin(principal));
//        return "notice/adminNoticeDetail";
//    }

    // ——————————————————————
    // 3) 공지 상세 (AJAX fragment 전용)
    // ——————————————————————
//    @GetMapping("/{id}/fragment")
//    public String noticeDetailFragment(
//        @PathVariable("id") Long id,
//        Model model,
//        Principal principal
//    ) {
//    	Notice notice = noticeService.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
//        model.addAttribute("notice", notice);
//        model.addAttribute("isAdmin", isAdmin(principal));
//        // detail.html 안의 <div id="noticeContent" th:fragment="noticeContent">
//        return "notice/adminNoticeDetail :: noticeContent";
//    }
    
    
    // 6. 상세 보기
//    @GetMapping("/{id}")
//    public String noticeDetail(@PathVariable("id") Long id,
//                               Model model,
//                               Principal principal) {
//        noticeService.incrementViewCount(id);
//        Notice notice = noticeService.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
//        model.addAttribute("notice", notice);
//        model.addAttribute("isAdmin", isAdmin(principal));
//        return "notice/detail";
//    }

    // 7. 삭제 처리
//    @PostMapping("/delete/{id}")
//    public String deleteNotice(@PathVariable("id") Long id,
//                               RedirectAttributes rttr) {
//        noticeService.deleteNotice(id);
//        rttr.addFlashAttribute("msg", "삭제되었습니다!");
//        return "redirect:/notice/noticeList";
//    }
}
