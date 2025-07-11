package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.entity.Jaso;
import com.example.demo.model.entity.JasoFeedback;
import com.example.demo.service.AIService;
import com.example.demo.service.JasoFeedbackService;
import com.example.demo.service.JasoService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.util.LinkedHashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/jaso")
public class JasoController {

    private final JasoService jasoService;
    private final JasoFeedbackService jasofeedbackService;
    private final AIService aiService;

    // ✅ 1. 자소서 목록 페이지
    @GetMapping("list")
    public String listJaso(Principal principal, Model model) {
        String userId = principal.getName(); // 현재 로그인 사용자 ID
        System.out.println("=== 자소서 목록 조회 ===");
        System.out.println("userId: " + userId);
        
        List<Jaso> jasoList = jasoService.getJasoByUserId(userId);
        System.out.println("jasoList size: " + (jasoList != null ? jasoList.size() : "null"));
        
        model.addAttribute("jasoList", jasoList);
        return "mypage/jasolist"; // → templates/mypage/jasolist.html
    }

    // ✅ 2. 자소서 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("jaso", new Jaso());
        return "mypage/jasowrite"; // → templates/mypage/jasowrite.html
    }
    
    // ✅ 2-1. 간단한 자소서 작성 폼 (테스트용)
    @GetMapping("/write-simple")
    public String writeSimpleForm(Model model) {
        model.addAttribute("jaso", new Jaso());
        return "mypage/jasowrite-simple"; // → templates/mypage/jasowrite-simple.html
    }

    // ✅ 3. 자소서 저장 처리 (기존 방식으로 변경)
    @PostMapping("/write")
    public String writeSubmit(@ModelAttribute Jaso jaso,
                             @RequestParam(value = "jaso_growth", required = false) String jasoGrowth,
                             @RequestParam(value = "jaso_personality", required = false) String jasoPersonality,
                             @RequestParam(value = "jaso_school", required = false) String jasoSchool,
                             @RequestParam(value = "jaso_motivation", required = false) String jasoMotivation,
                             @RequestParam(value = "jaso_future", required = false) String jasoFuture,
                             @RequestParam(value = "jaso_experience", required = false) String jasoExperience,
                             Principal principal) {
        System.out.println("=== 자소서 저장 시작 ===");
        String userId = principal.getName();
        jaso.setUserId(userId);
        jaso.setCreatedDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        jaso.setGrowth(jasoGrowth);
        jaso.setPersonality(jasoPersonality);
        jaso.setSchool(jasoSchool);
        jaso.setMotivation(jasoMotivation);
        jaso.setFuture(jasoFuture);
        jaso.setExperience(jasoExperience);
        try {
            Jaso savedJaso = jasoService.saveJaso(jaso);
            System.out.println("저장 성공! savedJaso.id: " + (savedJaso != null ? savedJaso.getId() : "null"));
        } catch (Exception e) {
            System.out.println("저장 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/mypage/jaso/list";
    }
    
    // ✅ 3-1. 간단한 자소서 저장 처리 (기존 방식)
    @PostMapping("/write-simple")
    public String writeSubmitSimple(@ModelAttribute Jaso jaso, Principal principal) {
        System.out.println("=== 간단한 자소서 저장 시작 ===");
        
        String userId = principal.getName();
        System.out.println("userId: " + userId);
        System.out.println("title: " + jaso.getTitle());
        // content 필드 제거됨
        
        jaso.setUserId(userId);
        jaso.setCreatedDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        try {
            Jaso savedJaso = jasoService.saveJaso(jaso);
            System.out.println("간단 저장 성공! savedJaso.id: " + (savedJaso != null ? savedJaso.getId() : "null"));
        } catch (Exception e) {
            System.out.println("간단 저장 실패: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "redirect:/mypage/jaso/list";
    }


    // ✅ 4. 자소서 상세 보기 + 피드백 목록
    @GetMapping("/view/{id}")
    public String viewJaso(@PathVariable("id") Long id, Model model) {
        System.out.println("=== 자소서 상세 조회 ===");
        System.out.println("jasoId: " + id);
        Jaso jaso = jasoService.getJasoById(id);
        System.out.println("jaso: " + (jaso != null ? "found" : "null"));
        if (jaso == null) {
            return "redirect:/mypage/jaso/list?error=not_found";
        }
        // 디버깅 정보
        System.out.println("jaso.title: " + jaso.getTitle());
        // content 관련 디버깅 제거 (필드 없음)

        // 섹션별 분리 (이제 content 파싱 대신 각 필드 사용)
        Map<String, String> sectionMap = new LinkedHashMap<>();
        sectionMap.put("성장과정", jaso.getGrowth());
        sectionMap.put("성격의 장단점", jaso.getPersonality());
        sectionMap.put("학창시절 및 동아리활동", jaso.getSchool());
        sectionMap.put("지원동기", jaso.getMotivation());
        sectionMap.put("입사 후 포부", jaso.getFuture());
        sectionMap.put("기타 특별한 경험", jaso.getExperience());
        model.addAttribute("sectionMap", sectionMap);

        List<JasoFeedback> feedbackList = jasofeedbackService.getFeedbackByJasoId(id);
        System.out.println("feedbackList size: " + (feedbackList != null ? feedbackList.size() : "null"));
        model.addAttribute("jaso", jaso);
        model.addAttribute("feedbackList", feedbackList);
        return "mypage/jasoview";
    }

    // ✅ 5. AI 피드백 생성 요청
    @PostMapping("/feedback/{id}")
    public String requestFeedback(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // 기존 피드백 모두 삭제
            List<JasoFeedback> oldFeedbacks = jasofeedbackService.getFeedbackByJasoId(id);
            if (oldFeedbacks != null) {
                for (JasoFeedback fb : oldFeedbacks) {
                    jasofeedbackService.deleteFeedback(fb.getId());
                }
            }
            // 새 피드백 생성 (모든 섹션을 합쳐서 프롬프트 생성)
            Jaso jaso = jasoService.getJasoById(id);
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("【성장과정】\n").append(jaso.getGrowth() != null ? jaso.getGrowth() : "").append("\n\n");
            promptBuilder.append("【성격의 장단점】\n").append(jaso.getPersonality() != null ? jaso.getPersonality() : "").append("\n\n");
            promptBuilder.append("【학창시절 및 동아리활동】\n").append(jaso.getSchool() != null ? jaso.getSchool() : "").append("\n\n");
            promptBuilder.append("【지원동기】\n").append(jaso.getMotivation() != null ? jaso.getMotivation() : "").append("\n\n");
            promptBuilder.append("【입사 후 포부】\n").append(jaso.getFuture() != null ? jaso.getFuture() : "").append("\n\n");
            promptBuilder.append("【기타 특별한 경험】\n").append(jaso.getExperience() != null ? jaso.getExperience() : "");
            String prompt = "다음 자기소개서를 읽고 피드백을 작성해주세요:\n" + promptBuilder.toString();
            String feedback = aiService.askChatGPT(prompt);
            JasoFeedback newFeedback = JasoFeedback.builder()
                    .jasoId(id)
                    .feedbackContent(feedback)
                    .createdDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();
            jasofeedbackService.saveFeedback(newFeedback);
            redirectAttributes.addFlashAttribute("message", "AI 피드백이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "AI 피드백 생성 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return "redirect:/mypage/jaso/view/" + id;
    }
    
    // ✅ 6. 자소서 삭제
    @GetMapping("/delete/{id}")
    public String deleteJaso(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            jasoService.deleteJaso(id);
            redirectAttributes.addFlashAttribute("message", "자소서가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "자소서 삭제 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return "redirect:/mypage/jaso/list";
    }

    @PostMapping("/update-section/{id}")
    public String updateSection(@PathVariable("id") Long id,
                               @RequestParam("section") String section,
                               @RequestParam("value") String value,
                               RedirectAttributes redirectAttributes) {
        try {
            Jaso jaso = jasoService.getJasoById(id);
            if (jaso == null) {
                redirectAttributes.addFlashAttribute("error", "자소서를 찾을 수 없습니다.");
                return "redirect:/mypage/jaso/view/" + id;
            }
            // 해당 섹션만 개별 필드로 수정
            switch (section) {
                case "성장과정":
                    jaso.setGrowth(value);
                    break;
                case "성격의 장단점":
                    jaso.setPersonality(value);
                    break;
                case "학창시절 및 동아리활동":
                    jaso.setSchool(value);
                    break;
                case "지원동기":
                    jaso.setMotivation(value);
                    break;
                case "입사 후 포부":
                    jaso.setFuture(value);
                    break;
                case "기타 특별한 경험":
                    jaso.setExperience(value);
                    break;
            }
            jasoService.saveJaso(jaso);
            redirectAttributes.addFlashAttribute("message", "수정이 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "수정 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return "redirect:/mypage/jaso/view/" + id;
    }
}
