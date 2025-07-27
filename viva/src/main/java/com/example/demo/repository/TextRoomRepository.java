package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.InterviewRoom;
import com.example.demo.model.entity.TextRoom;

@Repository
public interface TextRoomRepository extends JpaRepository<TextRoom, Integer> {

    // ✅ 특정 면접방과 연결된 텍스트룸 조회 (1:1 관계 가정)
    TextRoom findByIntrRoomId(InterviewRoom intrRoom);

    // ✅ 특정 host가 만든 모든 텍스트룸
    List<TextRoom> findByHost_UserId(String userId);

    // ✅ 상태코드로 필터링
    List<TextRoom> findByStatusCd(String statusCd);

    // 필요시: 특정 면접방 ID와 hostId로 조합 검색
    TextRoom findByIntrRoomIdAndHost_UserId(InterviewRoom intrRoom, String userId);
	
}
