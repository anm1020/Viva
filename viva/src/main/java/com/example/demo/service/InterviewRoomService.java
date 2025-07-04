package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.dto.InterviewRoomDTO;
import com.example.demo.model.entity.InterviewRoom;
import com.example.demo.repository.InterviewRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewRoomService {

	private final InterviewRoomRepository intrRoomRepo;
	
	// 방 전체 조회
	public List<InterviewRoomDTO> getAllRooms() {
        return intrRoomRepo.findAll().stream()		// 모든 인터뷰 방 목록 가져옴
                .map(this::convertToDTO)			// 각 엔티티 DTO 변환
                .collect(Collectors.toList());		// 다시 리스트 형태로 변환
    }

    // 방 생성
    public InterviewRoomDTO createRoom(InterviewRoomDTO dto) {
        InterviewRoom room = InterviewRoom.builder()
                .intrRoomTitle(dto.getIntrRoomTitle())
                .hostId(dto.getHostId())
                .statusCd(dto.getStatusCd())
                .build();

        return convertToDTO(intrRoomRepo.save(room));
    }

	
	// entity객체 -> DTO 객체로 변환 함수
	private InterviewRoomDTO convertToDTO(InterviewRoom entity) {
	    return InterviewRoomDTO.builder()
	            .intrRoomId(entity.getIntrRoomId())
	            .intrRoomTitle(entity.getIntrRoomTitle())
	            .hostId(entity.getHostId())
	            .createdDt(entity.getCreatedDt())
	            .startedDt(entity.getStartedDt())
	            .endedDt(entity.getEndedDt())
	            .statusCd(entity.getStatusCd())
	            .build();
	}
}
