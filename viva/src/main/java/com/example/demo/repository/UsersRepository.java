package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.entity.Users;

public interface UsersRepository extends JpaRepository<Users, String>{

	Users findByUserId(String userId); // Optional 없이!
//	Users save(UserDTO users);
//	Users findByUserId(String userId);

}
