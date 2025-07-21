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
        return "redirect:/resume/list";
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
        model.addAttribute("resume", resume);
        model.addAttribute("user", user);
        return "resu/resume-view";
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
        return "redirect:/resume/list";
    }

    // 데이터 디버깅용
    @GetMapping("/debug/{id}")
    public String debugResume(@PathVariable("id") int reId, Model model) {
        Resume resume = resumeService.getResumeById(reId).orElseThrow();
        model.addAttribute("resume", resume);
        return "resu/resume-debug";
    }
}
