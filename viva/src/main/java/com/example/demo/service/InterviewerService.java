package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.dto.InterviewerDTO;
import com.example.demo.model.dto.InterviewerDetailDTO;
import com.example.demo.model.entity.Interviewer;

public interface InterviewerService {
    List<InterviewerDTO> getInterviewerList();
    List<InterviewerDTO> getInterviewerListByCategory(String category);
    void save(Interviewer interviewer); 
    Optional<Interviewer> findById(Long id);
    InterviewerDetailDTO getInterviewerDetail(Long intrId);
    void deleteById(Long id);
}


