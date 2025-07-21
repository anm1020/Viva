package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.entity.Resume;
import com.example.demo.model.entity.Users;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    List<Resume> findByUser(Users user);
}
