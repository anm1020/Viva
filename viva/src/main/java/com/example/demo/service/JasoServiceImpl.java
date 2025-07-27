package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Jaso;
import com.example.demo.repository.JasoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JasoServiceImpl implements JasoService {

    private final JasoRepository jasoRepository;

    @Override
    public Jaso saveJaso(Jaso jaso) {
        return jasoRepository.save(jaso);
    }

    @Override
    public List<Jaso> getJasoListByUserId(String userId) {
        return jasoRepository.findByUserIdOrderByCreatedDtDesc(userId);
    }

    @Override
    public Jaso getJasoById(Long id) {
        return jasoRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteJaso(Long id) {
        jasoRepository.deleteById(id);
    }

    @Override
    public List<Jaso> getJasoByUserId(String userId) {
        return jasoRepository.findByUserIdOrderByCreatedDtDesc(userId);
    }
}
