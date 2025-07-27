package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.entity.Board;
import com.example.demo.model.entity.PointExchange;
import com.example.demo.model.entity.Users;
import com.example.demo.service.AdminService;
import com.example.demo.service.BoardService;
import com.example.demo.service.PointExchangeService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

//	private final Users users;
	private final AdminService adminService;
	private final UserService userService;
	private final PointExchangeService pointExchangeService;
	private final BoardService boardService;
	
	// 대시보드
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";   // templates/admin/main.html
    }
    
    // 대시보드 게시글 관리 메뉴탭
    /** 1. 게시글 리스트 (페이징, AJAX fragment) */
    @GetMapping("/admin/boardList")
    public String boardList(
    		 @RequestParam(name = "page", defaultValue = "0") int page,
    		 @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        Page<Board> boardPage = boardService.getBoardPage(PageRequest.of(page, size));
        model.addAttribute("boardPage", boardPage);
        return "admin/boardList :: boardListContent"; // fragment 반환
    }

    /** 2. 게시글 상세 (AJAX fragment) */
    @GetMapping("/admin/boardList/{id}")
    public String boardDetail(@PathVariable("id") Integer id, Model model) {
        Board board = boardService.getBoardById(id);
        if (board == null) throw new IllegalArgumentException("게시글 없음");
        model.addAttribute("board", board);
        return "admin/boardDetail :: boardDetailContent"; // fragment 반환
    }

    /** 3. 게시글 삭제 (AJAX) */
    @PostMapping("/admin/boardList/delete/{id}")
    @ResponseBody
    public String deleteBoard(@PathVariable("id") Integer id) {
        boardService.deleteBoard(id);
        return "success";
    }	// 게시물 관리 끝
    
    // 관리자 회원목록 페이지
//    @GetMapping("/admin/users")
//    public String userList(@RequestParam("userRole") String userRole, Model model) {
//        System.out.println(">>> [DEBUG] userRole: " + userRole);
//        List<Users> userList = adminService.getUserList(userRole);
//        System.out.println(">>> [DEBUG] userList.size: " + userList.size());
//        model.addAttribute("userList", userList);
//        model.addAttribute("selectedRole", userRole);
//        return "admin/users :: users";
//    }
//    @GetMapping("/admin/users")
//    public String userList(
//        @RequestParam("userRole") String userRole, Model model	) {
//    	System.out.println(">>> [DEBUG] userRole: " + userRole);
//        List<Users> userList = adminService.getUserList(userRole);
//        System.out.println(">>> [DEBUG] userList.size: " + userList.size());
//        model.addAttribute("userList", userList);
//        model.addAttribute("selectedRole", userRole);  // ★ 추가!
//        return "admin/users :: users";
//    }
//    
//    @GetMapping("/admin/users")
//    public String adminUsers(Model model,
//        @PageableDefault(size = 10, sort = "createdDt", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<Users> userPage = userService.getUserList(pageable);
//        model.addAttribute("users", userPage.getContent());
//        model.addAttribute("page", userPage);
//        return "admin/users";
//    }
    
    @GetMapping("/admin/users")
    public String userList(
        @RequestParam(value = "userRole", required = false) String userRole, 
        Model model,
        @PageableDefault(size = 10, sort = "createdDt", direction = Sort.Direction.DESC) Pageable pageable) 
    {
        Page<Users> userPage;
        if(userRole != null && !userRole.isBlank()) {
            userPage = adminService.getUserListByRole(userRole, pageable); // 관리자 서비스!
            model.addAttribute("selectedRole", userRole);
        } else {
            userPage = userService.getUserList(pageable); // 전체 목록(공통)
        }
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("page", userPage);
        return "admin/users";
    }
    
    @PostMapping("/admin/users/toggleStatus")
    @ResponseBody
    public Map<String, String> toggleUserStatus(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String userType = request.get("userType");

        Map<String, String> response = new HashMap<>();
        try {
            userService.updateUserType(userId, userType);
            response.put("result", "success");
        } catch(Exception e) {
            response.put("result", "fail");
        }
        return response;
    }
    
    @PostMapping("/admin/users/toggleOuth")
    @ResponseBody
    public Map<String, String> toggleUserOuth(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String userOuth = request.get("userOuth"); // "Y" 또는 "N"
        Map<String, String> response = new HashMap<>();
        try {
            userService.updateUserOuth(userId, userOuth);
            response.put("result", "success");
        } catch(Exception e) {
            response.put("result", "fail");
        }
        return response;
    }

    /** ▶ 대시보드 “포인트 환전” 메뉴 AJAX 로드용 프래그먼트 반환 */
    /** (수정) 모든 요청 조회 */
    /** 페이징 파라미터 받고 프래그먼트 반환 */
    @GetMapping("/admin/points1")
    public String pointsPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        Page<PointExchange> requestsPage =
            pointExchangeService.getRequests(page, size);
        model.addAttribute("requestsPage", requestsPage);
        // admin/points.html 에 정의된 th:fragment="adminContent" 부분만 리턴
        return "admin/points1 :: adminContent";
    }


    @PostMapping("/admin/approve/{id}")
    @ResponseBody
    public String approveExchange(@PathVariable("id") Long id) {
        pointExchangeService.approveExchange(id);
        return "승인 처리되었습니다.";
    }

    @PostMapping("/admin/reject/{id}")
    @ResponseBody
    public String rejectExchange(@PathVariable("id") Long id) {
        pointExchangeService.rejectExchange(id);
        return "거절 처리되었습니다.";
    }
    
    
    
    
    
    /**
     * 1) 대시보드 → 포인트 관리 메뉴 클릭 시
     *    PENDING 상태 환전 요청 목록만 모델에 담아 포인트관리 뷰 반환
     */
	/*
	 * @GetMapping("/admin/points") public String showPendingExchanges(Model model)
	 * { List<PointExchange> requests = pointExchangeService.getPendingRequests();
	 * model.addAttribute("requests", requests); return "admin/points"; //
	 * templates/admin/points.html }
	 * 
	 * // 1) 승인
	 * 
	 * @PostMapping("/approve/{id}")
	 * 
	 * @ResponseBody public String approveExchange(@PathVariable("id") Long
	 * exchangeId) { pointExchangeService.approveExchange(exchangeId, "APPROVED");
	 * return "승인 처리되었습니다."; }
	 * 
	 * // 2) 거절
	 * 
	 * @PostMapping("/reject/{id}")
	 * 
	 * @ResponseBody public String rejectExchange(@PathVariable("id") Long
	 * exchangeId) { pointExchangeService.rejectExchange(exchangeId, "REJECTED");
	 * return "거절 처리되었습니다."; }
	 */
}
