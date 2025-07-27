package com.example.demo.repository;

import com.example.demo.model.entity.Jaso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JasoRepository extends JpaRepository<Jaso, Long> {

    List<Jaso> findByUserIdOrderByCreatedDtDesc(String userId);
}
