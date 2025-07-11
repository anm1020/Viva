package com.example.demo.repository;

import com.example.demo.model.entity.JasoFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JasoFeedbackRepository extends JpaRepository<JasoFeedback, Long> {

    List<JasoFeedback> findByJasoIdOrderByCreatedDtDesc(Long jasoId);
}
