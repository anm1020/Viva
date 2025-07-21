package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private String rePosition;
    private String reStrengths;
    
    // 개인정보 (사용자 입력)
    private String reGitHub;
    private String reBlogNotion;
    
    // 학력
    private String reEducationPeriod;
    private String reEducationSchool;
    private String reEducationDetail;
    
    // 교육
    private String reTrainingPeriod;
    private String reTrainingInstitution;
    private String reTrainingCourse;
    
    // 주요경력
    private String reExperiencePeriod;
    private String reExperienceCompany;
    private String reExperiencePosition;
    
    // 자격증
    private String reCertificationsPeriod;
    private String reCertificationsInstitution;
    private String reCertificationsName;
    
    // 어학능력
    private String reLanguagesPeriod;
    private String reLanguagesLanguage;
    private String reLanguagesLevel;
    
    // 병역사항
    private String reMilitaryPeriod;
    private String reMilitaryStatus;
    private String reMilitaryDetail;
    
    // 프로젝트
    private String reProjectsPeriod;
    private String reProjectsTitle;
    private String reProjectsLink;
    private String reProjectsContent;
    
    // 기존 필드들 (호환성 유지)
    private String reEducation;
    private String reTraining;
    private String reExperience;
    private String reCertifications;
    private String reLanguages;
    private String reMilitary;
    private String reSkills;
    private String reProjects;
    private String reDetailExperience;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private String reCreatedAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private String reUpdatedAt;
}
