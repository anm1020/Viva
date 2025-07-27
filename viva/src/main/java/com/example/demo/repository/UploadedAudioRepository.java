package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.UploadedAudio;

@Repository
public interface UploadedAudioRepository extends JpaRepository<UploadedAudio, Integer> {

	// 방 번호로 검색
	List<UploadedAudio> findByInterviewRoom_IntrRoomId(Integer roomId);
	
	// 유저 ID로 검색
	List<UploadedAudio> findByUser_UserId(String userId);

	// 방번호, 유저 ID로 함께 검색 -> 아마 이걸쓸듯
	List<UploadedAudio> findByInterviewRoom_IntrRoomIdAndUser_UserId(Integer roomId, String userId);
}
