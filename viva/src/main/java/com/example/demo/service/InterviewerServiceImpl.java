package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.dto.InterviewerDTO;
import com.example.demo.model.dto.InterviewerDetailDTO;
import com.example.demo.model.entity.Interviewer;
import com.example.demo.repository.InterviewerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewerServiceImpl implements InterviewerService {

    private final InterviewerRepository interviewerRepository;

    @Override
    public List<InterviewerDTO> getInterviewerList() {
        return interviewerRepository.findAllForList();
    }
    
    @Override
    public List<InterviewerDTO> getInterviewerListByCategory(String category) {
        return interviewerRepository.findByCategory(category);
    }
    
    @Override
    public void save(Interviewer interviewer) {
        interviewerRepository.save(interviewer);
    }
    
    @Override
    public Optional<Interviewer> findById(Long id) {
        return interviewerRepository.findById(id);
    }
    
   
    @Override
    public InterviewerDetailDTO getInterviewerDetail(Long intrId) {
        return interviewerRepository.findDetailByIntrId(intrId)
            .orElseThrow(() -> new NoSuchElementException("면접관이 없습니다"));
    }

}