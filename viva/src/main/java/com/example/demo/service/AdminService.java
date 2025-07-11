package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Users;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final UsersRepository usersRepository;
    private final BoardRepository boardRepository;
    private final ReviewRepository reviewRepository;

    // 전체 회원 목록(최신 가입순) 반환
    public List<Users> getAllUsers() {
        return usersRepository.findAllByOrderByCreatedDtDesc();
    }

    public List<Users> getUserList(String userRole) {
        return usersRepository.findByUserRole(userRole);
    }
    
    // 회원 구분별 페이징 목록 조회
//    public Page<Users> getUserListByRole(String userRole, Pageable pageable) {
//        return usersRepository.findAllByUserRole(userRole, pageable);
//    }
    
    public Page<Users> getUserListByRole(String userRole, Pageable pageable) {
        return usersRepository.findAllByUserRole(userRole, pageable);
    }
    
    
    
    
//    public List<AdminUserListDTO> getUserList(String keyword) {
//        List<Users> userList;
//        if (keyword == null || keyword.isBlank()) {
//            userList = usersRepository.findAllByOrderByCreatedDtDesc();
//        } else {
//            userList = usersRepository.findByUserIdContainingOrUserNameContainingOrderByCreatedDtDesc(keyword, keyword);
//        }
//        // 회원별 게시물/리뷰 수 집계
//        List<AdminUserListDTO> result = new ArrayList<>();
//        for (Users user : userList) {
//            long boardCount = boardRepository.countByUsers(user);
//            long reviewCount = reviewRepository.countByUsers(user);
//            result.add(new AdminUserListDTO(
//                user.getUserId(),
//                user.getUserName(),
//                boardCount,
//                reviewCount,
//                user.getCreatedDt(),
//                user.getUserType()
//            ));
//        }
//        return result;
//    }

}