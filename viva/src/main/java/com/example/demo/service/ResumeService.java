package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Resume;
import com.example.demo.model.entity.Users;
import com.example.demo.repository.ResumeRepository;
import com.example.demo.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void save(Resume resume) {
        resumeRepository.save(resume);
    }

    @Transactional(readOnly = true)
    public List<Resume> getResumesByUserId(String userId) {
        Users user = usersRepository.findByUserId(userId).orElseThrow();
        return resumeRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Optional<Resume> getResumeById(int reId) {
        return resumeRepository.findById(reId);
    }
    @Transactional
    public void updateResume(int reId, Resume updated) {
        Resume resume = resumeRepository.findById(reId).orElseThrow();
        
        // 기본 정보
        resume.setReTitle(updated.getReTitle());
        resume.setRePosition(updated.getRePosition());
        resume.setReStrengths(updated.getReStrengths());
        
        // 개인정보 (사용자 입력)
        resume.setReGitHub(updated.getReGitHub());
        resume.setReBlogNotion(updated.getReBlogNotion());
        
        // 학력
        resume.setReEducationPeriod(updated.getReEducationPeriod());
        resume.setReEducationSchool(updated.getReEducationSchool());
        resume.setReEducationDetail(updated.getReEducationDetail());
        
        // 교육
        resume.setReTrainingPeriod(updated.getReTrainingPeriod());
        resume.setReTrainingInstitution(updated.getReTrainingInstitution());
        resume.setReTrainingCourse(updated.getReTrainingCourse());
        
        // 주요경력
        resume.setReExperiencePeriod(updated.getReExperiencePeriod());
        resume.setReExperienceCompany(updated.getReExperienceCompany());
        resume.setReExperiencePosition(updated.getReExperiencePosition());
        
        // 자격증
        resume.setReCertificationsPeriod(updated.getReCertificationsPeriod());
        resume.setReCertificationsInstitution(updated.getReCertificationsInstitution());
        resume.setReCertificationsName(updated.getReCertificationsName());
        
        // 어학능력
        resume.setReLanguagesPeriod(updated.getReLanguagesPeriod());
        resume.setReLanguagesLanguage(updated.getReLanguagesLanguage());
        resume.setReLanguagesLevel(updated.getReLanguagesLevel());
        
        // 병역사항
        resume.setReMilitaryPeriod(updated.getReMilitaryPeriod());
        resume.setReMilitaryStatus(updated.getReMilitaryStatus());
        resume.setReMilitaryDetail(updated.getReMilitaryDetail());
        
        // 프로젝트
        resume.setReProjectsPeriod(updated.getReProjectsPeriod());
        resume.setReProjectsTitle(updated.getReProjectsTitle());
        resume.setReProjectsLink(updated.getReProjectsLink());
        resume.setReProjectsContent(updated.getReProjectsContent());
        
        // 기존 필드들 (호환성 유지)
        resume.setReEducation(updated.getReEducation());
        resume.setReTraining(updated.getReTraining());
        resume.setReExperience(updated.getReExperience());
        resume.setReCertifications(updated.getReCertifications());
        resume.setReLanguages(updated.getReLanguages());
        resume.setReMilitary(updated.getReMilitary());
        resume.setReSkills(updated.getReSkills());
        resume.setReProjects(updated.getReProjects());
        resume.setReDetailExperience(updated.getReDetailExperience());
        
        // updatedAt은 자동 처리됨
    }

    @Transactional
    public void deleteResume(int reId) {
        resumeRepository.deleteById(reId);
    }

}
