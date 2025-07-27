package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.entity.Resume;
import com.example.demo.model.entity.Users;
import com.example.demo.service.ResumeService;
import com.example.demo.repository.UsersRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final UsersRepository usersRepository;

    @GetMapping("/write")
    public String showWriteForm(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        Users user = usersRepository.findByUserId(userId).orElseThrow();
        model.addAttribute("resume", new Resume());
        model.addAttribute("user", user);
        return "resu/resume-write";
    }

    @PostMapping("/write")
    public String submitResume(@ModelAttribute Resume resume, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        Users user = usersRepository.findByUserId(userId).orElseThrow();
        resume.setUser(user);
        resumeService.save(resume);
        return "redirect:/mypage";
    }

    @GetMapping("/list")
    public String listResumes(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("resumes", resumeService.getResumesByUserId(userId));
        return "resu/resume-list";
    }

    @GetMapping("/view/{id}")
    public String viewResume(@PathVariable("id") int reId, Model model) {
        Resume resume = resumeService.getResumeById(reId).orElseThrow();
        // 사용자 정보를 별도로 로드하여 전달
        Users user = resume.getUser();
        
        // 빈 배열 방어 로직 추가
        processResumeArrays(resume);
        
        model.addAttribute("resume", resume);
        model.addAttribute("user", user);
        return "resu/resume-view";
    }
    
    // 빈 배열 방어를 위한 헬퍼 메서드
    private void processResumeArrays(Resume resume) {
        // 각 필드가 null이거나 빈 문자열인 경우 빈 배열로 처리
        if (resume.getReTrainingPeriod() == null || resume.getReTrainingPeriod().trim().isEmpty()) {
            resume.setReTrainingPeriod("");
        }
        if (resume.getReTrainingInstitution() == null || resume.getReTrainingInstitution().trim().isEmpty()) {
            resume.setReTrainingInstitution("");
        }
        if (resume.getReTrainingCourse() == null || resume.getReTrainingCourse().trim().isEmpty()) {
            resume.setReTrainingCourse("");
        }
        
        if (resume.getReExperiencePeriod() == null || resume.getReExperiencePeriod().trim().isEmpty()) {
            resume.setReExperiencePeriod("");
        }
        if (resume.getReExperienceCompany() == null || resume.getReExperienceCompany().trim().isEmpty()) {
            resume.setReExperienceCompany("");
        }
        if (resume.getReExperiencePosition() == null || resume.getReExperiencePosition().trim().isEmpty()) {
            resume.setReExperiencePosition("");
        }
        
        if (resume.getReCertificationsPeriod() == null || resume.getReCertificationsPeriod().trim().isEmpty()) {
            resume.setReCertificationsPeriod("");
        }
        if (resume.getReCertificationsInstitution() == null || resume.getReCertificationsInstitution().trim().isEmpty()) {
            resume.setReCertificationsInstitution("");
        }
        if (resume.getReCertificationsName() == null || resume.getReCertificationsName().trim().isEmpty()) {
            resume.setReCertificationsName("");
        }
        
        if (resume.getReLanguagesPeriod() == null || resume.getReLanguagesPeriod().trim().isEmpty()) {
            resume.setReLanguagesPeriod("");
        }
        if (resume.getReLanguagesLanguage() == null || resume.getReLanguagesLanguage().trim().isEmpty()) {
            resume.setReLanguagesLanguage("");
        }
        if (resume.getReLanguagesLevel() == null || resume.getReLanguagesLevel().trim().isEmpty()) {
            resume.setReLanguagesLevel("");
        }
        
        if (resume.getReProjectsPeriod() == null || resume.getReProjectsPeriod().trim().isEmpty()) {
            resume.setReProjectsPeriod("");
        }
        if (resume.getReProjectsTitle() == null || resume.getReProjectsTitle().trim().isEmpty()) {
            resume.setReProjectsTitle("");
        }
        if (resume.getReProjectsLink() == null || resume.getReProjectsLink().trim().isEmpty()) {
            resume.setReProjectsLink("");
        }
        if (resume.getReProjectsContent() == null || resume.getReProjectsContent().trim().isEmpty()) {
            resume.setReProjectsContent("");
        }
    }
    
    @GetMapping("/edit/{id}")
    public String editResumeForm(@PathVariable("id") int reId, Model model) {
        Resume resume = resumeService.getResumeById(reId).orElseThrow();
        // 사용자 정보를 별도로 로드하여 전달
        Users user = resume.getUser();
        model.addAttribute("resume", resume);
        model.addAttribute("user", user);
        return "resu/resume-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateResume(@PathVariable("id") int reId, @ModelAttribute Resume resume) {
        resumeService.updateResume(reId, resume);
        return "redirect:/resume/view/" + reId;
    }

    @PostMapping("/delete/{id}")
    public String deleteResume(@PathVariable("id") int reId) {
        resumeService.deleteResume(reId);
        return "redirect:/mypage";
    }

    // 데이터 디버깅용
    @GetMapping("/debug/{id}")
    public String debugResume(@PathVariable("id") int reId, Model model) {
        Resume resume = resumeService.getResumeById(reId).orElseThrow();
        model.addAttribute("resume", resume);
        return "resu/resume-debug";
    }
}
