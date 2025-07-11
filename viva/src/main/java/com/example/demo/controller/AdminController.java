package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.entity.Users;
import com.example.demo.service.AdminService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

//	private final Users users;
	private final AdminService adminService;
	private final UserService userService;
	
	// 대시보드
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";   // templates/admin/main.html
    }
    
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

    
}
