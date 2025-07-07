package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.entity.Users;
import com.example.demo.security.CustomUserDetails;
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

		Users users = new Users();
	    users.setUserRole(role);       // 엔티티에 바로 값 셋팅
	    model.addAttribute("users", users);
	    model.addAttribute("role", role); // 필요하면 role도 넘기기
		
//	    if (role.equals("intr")) {
//	        return "member/interviewerform"; // 면접관 폼
//	    } else {
	        return "member/memberform"; 	// 취준생 폼
	    	  
	}
	
	// 회원가입
	@PostMapping("memberinsert")
	public String memberinsert(@ModelAttribute Users users,
								RedirectAttributes rttr,
							   // ★ 다중 선택된 기술 배열 받기
						       @RequestParam(name = "skill", required = false) List<String> UserSkill,
						       Model model) {
		
		 // 1) 이메일 합치기
		String fullEmail = users.getEmailId() + "@" + users.getEmailDomain();
		users.setUserEmail(fullEmail);
		
	    // 2) 휴대폰번호 합치기
		String fullPhone = users.getPhonePrefix() + "-" + users.getPhoneMiddle() + "-" + users.getPhoneLast();
	    users.setUserNum(fullPhone);
	    
	    // 체크박스 값 하이픈(-) 결합
	    String allSkill = "";
	    if (UserSkill != null && !UserSkill.isEmpty()) {
	        allSkill = String.join(",", UserSkill);
	    }
	    users.setUserSkill(allSkill);
	    
	    
	    // 커리어(경력) 입력란 두 개를 하나로 합쳐 userCareer 컬럼에 세팅
	 	String career = users.getUserCareer() + "/" + users.getUserCareer2();
	 	users.setUserCareer(career);

	    
//	     **★★ user_id 중복 체크 후 저장!★★**
	    if (service.isUserIdDuplicate(users.getUserId())) {
	        model.addAttribute("users", users); // 폼 값 보존
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
	    	service.save(users);
	        // 성공 메시지 & 로그인 페이지로 리다이렉트
	        rttr.addFlashAttribute("registerMsg", "회원가입에 성공했습니다!\n로그인 후 이용해주세요.");
	        return "redirect:/loginmain";
	    } catch (Exception e) {
	        // 8. 예외 발생 시 실패 메시지 & 폼으로 리다이렉트
	        rttr.addFlashAttribute("registerFailMsg", "회원가입에 실패했습니다.\n형식에 맞게 정보를 다시 입력해주세요.");
	        return "redirect:/memberform?role=" + users.getUserRole();
	    }
	}
	
	// 아이디 중복 검사
	@ResponseBody
    @GetMapping("/check-id")
    public String checkUserId(@RequestParam("userId") String userId) {
		boolean exists = service.isUserIdDuplicate(userId);
	    return exists ? "duplicated" : "ok";
    }
    
	// 로그인
//	@PostMapping("/loginAll")
//	public String studentLogin(@RequestParam("userId") String userId,
//								@RequestParam("password") String password, RedirectAttributes rttr,
//								@RequestParam("role") String role,
//								Model model,
//								HttpSession session) {
//
//		// 1. 입력값 null 체크
//	    if (userId == null || userId.trim().isEmpty() ||
//	        password == null || password.trim().isEmpty()) {
//	        rttr.addFlashAttribute("loginMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
//	        return "redirect:/loginmain";
//	    }
//	
//	    // 2. DB에서 유저 찾기
//	    Users users = service.login(userId); // userId로 찾는 메서드
//	    
//	    if (users == null) {
//	        rttr.addFlashAttribute("loginMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
//	        return "redirect:/loginmain";
//	    }
//	
//	    // 3. 비밀번호 일치 여부
//	    if (!users.getUserPass().equals(password)) {
//	        rttr.addFlashAttribute("loginMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
//	        return "redirect:/loginmain";
//	    }
//	
//	    // 4. role 일치 여부 (로그인 화면에서 role도 넘어오면 비교)
//	    if (!role.equals(users.getUserRole())) {
//	        rttr.addFlashAttribute("loginMsg", "권한이 일치하지 않습니다.");
//	        return "redirect:/loginmain";
//	    }
//	    
//	    
//	    // 로그인 성공
//	    session.setAttribute("user_id", users.getUserId());
//	    session.setAttribute("user_role", users.getUserRole());
//	    model.addAttribute("loginMsg", "로그인에 성공했습니다.");
//	    model.addAttribute("targetUrl", "main"); // 메인페이지로 이동
//	    
//	    return "member/loginSuccess"; // loginSuccess.html 뷰로 이동
//	}
	   
		// 로그아웃
		@GetMapping("/logout")
		public String logout(HttpSession session) {
		    session.invalidate();
		    return "redirect:/main";
		}
		
		// 메인 페이지
		@GetMapping("/main")
		public String main(Authentication authentication, Model model
//							@RequestParam("role") String role
							) {
			
			 // 1. 인증 객체 가져오기
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	        String displayName = null;
	        String role = null;

	        // 2. 인증됐고 익명유저 아니면
	        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
	            Object principal = auth.getPrincipal();

	            // (1) 일반 로그인 (UserDetails)
	            if (principal instanceof CustomUserDetails userDetails) {
	                displayName = userDetails.getUsers().getUserName(); // 엔티티 필드에 따라 변경!
	                role = userDetails.getUsers().getUserRole();           // 예: "mem" 또는 "intr"
	            }
	            
	            // (2) 소셜 로그인 (DefaultOAuth2User)
	            else if (principal instanceof DefaultOAuth2User oauth2User) {
	                // 카카오 구조: properties.nickname (속성명 구조에 따라 달라질 수 있음)
	                Map<String, Object> attrs = oauth2User.getAttributes();
	                if (attrs != null && attrs.containsKey("properties")) {
	                    Map<String, Object> props = (Map<String, Object>) attrs.get("properties");
	                    if (props != null && props.containsKey("nickname")) {
	                        displayName = (String) props.get("nickname");
	                    }
	                }
	                // (참고) 네이버 등은 구조가 다를 수 있으니 확인 필요
	            }
	        }

	        model.addAttribute("displayName", displayName);
	        model.addAttribute("role", role);

	        return "main"; // main.html (Thymeleaf)
	    }
		
		
		// Service에서 현재 로그인 사용자의 정보 조회
		@GetMapping("/intrMypage")
		public String mypage(Model model, Principal principal) {
		    // principal.getName() == 현재 로그인한 아이디(또는 PK)
		    String userId = principal.getName();

		    // DB에서 유저 정보 조회 (UserService가 Repository 사용)
		    Users users = service.findByUserId(userId);
		    model.addAttribute("users", users);

		    return "member/intrMypage"; // mypage.html (Thymeleaf)
		}
		
		@GetMapping("/memberEdit")
		public String showEditForm(Model model, Principal principal,
						@RequestParam(name = "skill", required = false) List<String> UserSkill) {
		    // 1. 현재 로그인 유저의 아이디(또는 PK) 구하기
		    String userId = principal.getName();

		    // 2. DB에서 회원정보 조회
		    Users users = service.findByUserId(userId);

		    // 1. 핸드폰 번호 분리
		    if (users.getUserNum() != null) {
		        String[] phone = users.getUserNum().split("-");
		        if (phone.length == 3) {
		            users.setPhonePrefix(phone[0]);
		            users.setPhoneMiddle(phone[1]);
		            users.setPhoneLast(phone[2]);
		        }
		    }
		    
		    // 이메일 분리
		    if (users.getUserEmail() != null) {
		        String[] email = users.getUserEmail().split("@");
		        if (email.length == 2) {
		            users.setEmailId(email[0]);
		            users.setEmailDomain(email[1]);
		        }
		    }
		    
		    // userSkill(String) → 배열로 변환 (체크박스 자동 체크용) 분리
		    if (users.getUserSkill() != null && !users.getUserSkill().isBlank()) {
		        String[] skillArr = users.getUserSkill().split(",");
		        model.addAttribute("userSkillArr", skillArr);
		    } else {
		        model.addAttribute("userSkillArr", new String[0]);
		    }
		    
		    // 경력 분리: "삼성전자/백엔드/3년" => [삼성전자, 백엔드, 3년]
		    // 경력 분리 (ex. "삼성전자/5년" → "삼성전자", "5년")
		    if (users.getUserCareer() != null) {
		        String[] careerArr = users.getUserCareer().split("/", 2); // 2개까지만 분리
		        users.setUserCareer(careerArr.length > 0 ? careerArr[0] : "");
		        users.setUserCareer2(careerArr.length > 1 ? careerArr[1] : "");
		    } else {
		        users.setUserCareer("");
		        users.setUserCareer2("");
		    }
		    
		    // 3. 모델에 담아서 폼으로 전달
		    model.addAttribute("users", users);

		    return "member/memberEdit"; // editMember.html (Thymeleaf)
		}
		
		@PostMapping("/memberUpdate")
		public String updateMemberInfo(@ModelAttribute("users") Users users, 
										@RequestParam(name = "skill", required = false) List<String> UserSkill,
										Principal principal, RedirectAttributes rttr) {
		    // 1. 로그인 사용자 확인(보안)
		    String userId = principal.getName();

		    // 2. 기존 정보 불러와서(아이디 등)  
		    Users dbUser = service.findByUserId(userId);

		    // 2. 핸드폰 (3개 → 1개로 합쳐서 저장)
		    String fullPhone = 
		        (users.getPhonePrefix() != null ? users.getPhonePrefix() : "") + "-" +
		        (users.getPhoneMiddle() != null ? users.getPhoneMiddle() : "") + "-" +
		        (users.getPhoneLast() != null ? users.getPhoneLast() : "");
		    dbUser.setUserNum(fullPhone);
		    
		    // 경력 합치기 (수정 폼에서 받은 값)
		    String career = (users.getUserCareer() != null ? users.getUserCareer() : "") + "/" +
		                    (users.getUserCareer2() != null ? users.getUserCareer2() : "");
		    dbUser.setUserCareer(career);
		    
		    // 5. 기술(체크박스 여러개 → 콤마로 합쳐서 저장)
//		    if (users.getUserSkill() != null && users.getUserSkill().length() > 0) {
//		        String allSkill = String.join(",", users.getUserSkill());
//		        dbUser.setUserSkill(allSkill);
//		    } else {
//		        dbUser.setUserSkill("");
//		    }
		    
		    // 기술 String → 배열로 변환해서 model에 "userSkillArr"로 전달: 체크박스의 th:checked에 사용됨.
		    if (UserSkill != null && !UserSkill.isEmpty()) {
		        String allSkill = String.join(",", UserSkill);
		        dbUser.setUserSkill(allSkill);
		    } else {
		        dbUser.setUserSkill("");
		    }
		    
		    // 3. 수정 가능한 필드만 업데이트
		    dbUser.setUserBirth(users.getUserBirth());
		    dbUser.setUserGender(users.getUserGender());
		    dbUser.setUserAdd(users.getUserAdd());
		    dbUser.setUserAddDetail(users.getUserAddDetail());

		    // 4. 저장
		    service.save(dbUser);

		    // 5. 성공 메시지 + 마이페이지로 이동
		    rttr.addFlashAttribute("editSuccess", "회원정보가 수정되었습니다!");
		    return "redirect:/intrMypage"; // or memMypage (role에 따라)
		}
		
}