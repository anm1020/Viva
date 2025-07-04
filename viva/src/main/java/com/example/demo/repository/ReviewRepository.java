package com.example.demo.repository;

import com.example.demo.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByIntrIdOrderByCreatedDtDesc(Integer intrId);
} 