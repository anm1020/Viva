package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.InterviewRoomDTO;
import com.example.demo.model.dto.TextRoomDTO;
import com.example.demo.model.entity.InterviewRoom;
import com.example.demo.model.entity.TextRoom;
import com.example.demo.model.entity.Users;
import com.example.demo.repository.InterviewRoomRepository;
import com.example.demo.repository.TextRoomRepository;
import com.example.demo.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewRoomService {

	@Autowired
	private final UsersRepository usersRepo; 
	
	@Autowired
	private final InterviewRoomRepository intrRoomRepo;
	
	@Autowired
	private final TextRoomRepository textRoomRepo;
	
	// 방 전체 조회
	public List<InterviewRoomDTO> getAllRooms() {
        return intrRoomRepo.findAll().stream()					// 모든 인터뷰 방 목록 가져옴
                .map(this::interviewRoomConvertToDTO)			// 각 엔티티 DTO 변환
                .collect(Collectors.toList());					// 다시 리스트 형태로 변환
    }

	// 방생성
	@Transactional
	public InterviewRoomDTO createRoomWithText(InterviewRoomDTO dto) {
		System.out.println("HostID : " + dto.getHostId());
		
	    Users host = usersRepo.findById(dto.getHostId())
	        .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

	    InterviewRoom room = InterviewRoom.builder()
	        .intrRoomTitle(dto.getIntrRoomTitle())
	        .host(host)
	        .statusCd(dto.getStatusCd())
	        .build();

	    InterviewRoom savedRoom = intrRoomRepo.save(room);
	    System.out.println("🔨 방 생성 성공: " + savedRoom.getIntrRoomId());
	    TextRoom textRoom = TextRoom.builder()
	        .intrRoomId(savedRoom)
	        .host(host)
	        .textRoomTitle(savedRoom.getIntrRoomTitle() + " - 채팅방")
	        .statusCd("active")
	        .build();

	    textRoomRepo.save(textRoom);
	    System.out.println("💬 채팅방 생성 시도...");

	    return interviewRoomConvertToDTO(savedRoom);
	}
	
//    // 방 생성
//	@Transactional
//    public InterviewRoomDTO createRoom(InterviewRoomDTO dto) {
//    	Users host = usersRepo.findById(dto.getHostId())
//    			.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다: " + dto.getHostId()));
//    	
//    	InterviewRoom room = InterviewRoom.builder()
//                .intrRoomTitle(dto.getIntrRoomTitle())
//                .host(host)
//                .statusCd(dto.getStatusCd())
//                .build();
//
//        return interviewRoomConvertToDTO(intrRoomRepo.save(room));
//    }
//
//    // 채팅방 생성
//	@Transactional
//    public TextRoomDTO createTextRoom(TextRoomDTO dto) {
//		System.out.println("채팅방 생성 호출 성공");
//        InterviewRoom room = intrRoomRepo.findById(dto.getIntrRoomId())
//            .orElseThrow(() -> new IllegalArgumentException("면접방이 존재하지 않습니다."));
//        Users host = usersRepo.findById(dto.getHostId())
//            .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
//
//        TextRoom textRoom = TextRoom.builder()
//            .intrRoomId(room)
//            .host(host)
//            .textRoomTitle(dto.getTextRoomTitle())
//            .statusCd(dto.getStatusCd())
//            .build();
//
//        return textRoomConvertToDto(textRoomRepo.save(textRoom));
//    }
	
	// entity객체 -> DTO 객체로 변환 함수
    // interviewRoom
	private InterviewRoomDTO interviewRoomConvertToDTO(InterviewRoom entity) {
	    return InterviewRoomDTO.builder()
	            .intrRoomId(entity.getIntrRoomId())
	            .intrRoomTitle(entity.getIntrRoomTitle())
	            .hostId(entity.getHost().getUserId())
	            .createdDt(entity.getCreatedDt())
	            .startedDt(entity.getStartedDt())
	            .endedDt(entity.getEndedDt())
	            .statusCd(entity.getStatusCd())
	            .build();
	}
	
	// textRoom
	public TextRoomDTO textRoomConvertToDto(TextRoom entity) {
	    return TextRoomDTO.builder()
	        .textRoomId(entity.getTextRoomId())
	        .intrRoomId(entity.getIntrRoomId().getIntrRoomId())   // FK에서 ID 꺼냄
	        .hostId(entity.getHost().getUserId())                 // Users 객체에서 ID 꺼냄
	        .textRoomTitle(entity.getTextRoomTitle())
	        .createdDt(entity.getCreatedDt())
	        .statusCd(entity.getStatusCd())
	        .build();
	}
}
