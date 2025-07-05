package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

	User save(UserDTO user);

//	User save(UserDTO user);
	User findByUserId(String userId);


}
