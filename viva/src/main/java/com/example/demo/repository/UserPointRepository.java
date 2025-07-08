package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.UserPoint;
import com.example.demo.model.entity.Users;

public interface UserPointRepository extends JpaRepository<UserPoint, String> {
	
	  Optional<UserPoint> findByUserId(String userId);
}
