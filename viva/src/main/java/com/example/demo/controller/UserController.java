package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.entity.User;
//import com.example.demo.model.entity.User.UserRole;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService service;
	
	// 로그인 및 회원가입 메인
	@GetMapping("/loginmain")
	public String loginmain() {
		return "member/loginmain";
	}
	
	// 회원가입폼 
	@GetMapping("/memberform")
	public String memberform(@RequestParam("role") String role, 
//							@RequestParam("userType") String userType,
							Model model) {
//	    UserDTO userDto = new UserDTO();
//	    userDto.setUserRole(role);
//	    model.addAttribute("user", userDto);

		User user = new User();
	    user.setUserRole(role);       // 엔티티에 바로 값 셋팅
	    model.addAttribute("user", user);
	    model.addAttribute("role", role); // 필요하면 role도 넘기기
		
//	    if (role.equals("intr")) {
//	        return "member/interviewerform"; // 면접관 폼
//	    } else {
	        return "member/memberform"; 	// 취준생 폼
	    	  
	}
	
	// 회원가입
	@PostMapping("memberinsert")
	public String memberinsert(@ModelAttribute User user,
								RedirectAttributes rttr,
							   // ★ 다중 선택된 기술 배열 받기
						       @RequestParam(name = "skill", required = false) List<String> UserSkill,
						       Model model) {
		
		 // 1) 이메일 합치기
		String fullEmail = user.getEmailId() + "@" + user.getEmailDomain();
		user.setUserEmail(fullEmail);
		
	    // 2) 휴대폰번호 합치기
		String fullPhone = user.getPhonePrefix() + "-" + user.getPhoneMiddle() + "-" + user.getPhoneLast();
	    user.setUserNum(fullPhone);
	    
	    // 체크박스 값 하이픈(-) 결합
	    String allSkill = "";
	    if (UserSkill != null && !UserSkill.isEmpty()) {
	        allSkill = String.join(",", UserSkill);
	    }
	    user.setUserSkill(allSkill);
	    
	    
	    // 커리어(경력) 입력란 두 개를 하나로 합쳐 userCareer 컬럼에 세팅
	 	String career = user.getUserCareer() + "/" + user.getUserCareer2();
	 	user.setUserCareer(career);

	    
//	     **★★ user_id 중복 체크 후 저장!★★**
	    if (service.isUserIdDuplicate(user.getUserId())) {
	        model.addAttribute("user", user); // 폼 값 보존
	        model.addAttribute("msg", "이미 사용중인 아이디입니다.");
	        return "member/memberform"; // 취준생 폼 경로
	        // 또는 return "member/interviewerform"; // 역할(role)에 따라 분기 필요!
	    }
	    
	    // 비밀번호, 비번확인 일치 검사
//	    if (!user.getUserPass().equals(user.getUserPassConfirm())) {
//	        model.addAttribute("user", user);
//	        model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
//	        return "member/memberform";
//	    }
	    
	    try {
	    	service.save(user);
	        // 성공 메시지 & 로그인 페이지로 리다이렉트
	        rttr.addFlashAttribute("registerMsg", "회원가입에 성공했습니다!\n로그인 후 이용해주세요.");
	        return "redirect:/loginmain";
	    } catch (Exception e) {
	        // 8. 예외 발생 시 실패 메시지 & 폼으로 리다이렉트
	        rttr.addFlashAttribute("registerFailMsg", "회원가입에 실패했습니다.\n형식에 맞게 정보를 다시 입력해주세요.");
	        return "redirect:/memberform?role=" + user.getUserRole();
	    }
	}
	
	// 아이디 중복 검사
	@ResponseBody
    @GetMapping("/check-id")
    public String checkUserId(@RequestParam("userId") String userId) {
		boolean exists = service.isUserIdDuplicate(userId);
	    return exists ? "duplicated" : "ok";
    }
    
//	@PostMapping("/register")
//	public String register(@ModelAttribute User user, Model model) {
//	    try {
//	        service.registerUser(user);
//	        return "redirect:/login"; // 성공 시 로그인 페이지 등으로 이동
//	    } catch (IllegalArgumentException e) {
//	        model.addAttribute("msg", e.getMessage());
//	        return "memberform"; // 실패 시 회원가입 폼으로 다시
//	    }
//	}

	// 로그인
	@PostMapping("/loginAll")
	public String studentLogin(@RequestParam("userId") String userId,
								@RequestParam("password") String password, RedirectAttributes rttr,
								@RequestParam("role") String role,
								Model model,
								HttpSession session) {

		// 1. 입력값 null 체크
	    if (userId == null || userId.trim().isEmpty() ||
	        password == null || password.trim().isEmpty()) {
	        rttr.addFlashAttribute("loginMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
	        return "redirect:/loginmain";
	    }
	
	    // 2. DB에서 유저 찾기
	    User user = service.login(userId); // userId로 찾는 메서드
	    
	    if (user == null) {
	        rttr.addFlashAttribute("loginMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
	        return "redirect:/loginmain";
	    }
	
	    // 3. 비밀번호 일치 여부
	    if (!user.getUserPass().equals(password)) {
	        rttr.addFlashAttribute("loginMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
	        return "redirect:/loginmain";
	    }
	
	    // 4. role 일치 여부 (로그인 화면에서 role도 넘어오면 비교)
	    if (!role.equals(user.getUserRole())) {
	        rttr.addFlashAttribute("loginMsg", "권한이 일치하지 않습니다.");
	        return "redirect:/loginmain";
	    }
	    
	    
	    // 로그인 성공
	    session.setAttribute("user_id", user.getUserId());
	    session.setAttribute("user_role", user.getUserRole());
	    model.addAttribute("loginMsg", "로그인에 성공했습니다.");
	    model.addAttribute("targetUrl", "main"); // 메인페이지로 이동
	    
	    return "member/loginSuccess"; // loginSuccess.html 뷰로 이동
	}
	    
	    
//	    if (user.getUserPass().equals(password)) {
//	    	session.setAttribute("user_id", user.getUserId());
//	    	System.out.println("로그인 성공");
//	        rttr.addFlashAttribute("loginMsg", "로그인에 성공했습니다.");
//	        return "redirect:/main";  // 또는 "/"
//	    } else {
//	        rttr.addFlashAttribute("loginMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
//	        return "redirect:/loginmain";  // 다시 로그인 폼으로
//	    }
	
	@GetMapping("main")
	public String mainpage() {
		
		return "member/main";
	}
	
	
	
}