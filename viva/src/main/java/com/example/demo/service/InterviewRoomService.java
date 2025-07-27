package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.InterviewRoomDTO;
import com.example.demo.model.dto.TextRoomDTO;
import com.example.demo.model.entity.InterviewRoom;
import com.example.demo.model.entity.Reservation;
import com.example.demo.model.entity.TextRoom;
import com.example.demo.model.entity.Users;
import com.example.demo.repository.InterviewRoomRepository;
import com.example.demo.repository.ReservationRepository;
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
	
	@Autowired
	private final ReservationRepository reservationRepo;
	
	// 방 전체 조회
	public List<InterviewRoomDTO> getAllRooms() {
        return intrRoomRepo.findAllByOrderByIntrRoomIdDesc()
        		.stream()										// 모든 인터뷰 방 목록  가져옴
                .map(this::interviewRoomConvertToDTO)			// 각 엔티티 DTO 변환
                .collect(Collectors.toList());					// 다시 리스트 형태로 변환
    }

	// 예약번호로 방검색
	public InterviewRoomDTO getRoomByResId(Integer resId) {
	    InterviewRoom entity = intrRoomRepo.findByReservation_ResId(resId)
	        .orElseThrow(() -> new IllegalArgumentException("해당 예약(resId)에 방이 존재하지 않습니다."));
	    return interviewRoomConvertToDTO(entity);
	}
	
	// 방 생성 중복 검사
	@Transactional
	public InterviewRoomDTO createRoomSafely(InterviewRoomDTO dto) {
	    if (dto.getResId() != null) {
	        Reservation reservation = reservationRepo.findById(dto.getResId())
	            .orElseThrow(() -> new IllegalStateException("해당 예약(resId)을 찾을 수 없습니다."));

	        if (intrRoomRepo.existsByReservation(reservation)) {
	            throw new IllegalStateException("이미 생성된 방이 있습니다.");
	        }
	    }

	    return createRoomWithText(dto);  // 기존 생성 로직 재사용
	}
	
	// 방생성
	@Transactional
	public InterviewRoomDTO createRoomWithText(InterviewRoomDTO dto) {
		System.out.println("HostID : " + dto.getHostId());
		
	    Users host = usersRepo.findById(dto.getHostId())
	        .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

	    Reservation reservation = null;
	    if (dto.getResId() != null) {
	        reservation = reservationRepo.findById(dto.getResId())
	            .orElseThrow(() -> new IllegalArgumentException("예약 없음"));
	    }
	    
	    InterviewRoom room = InterviewRoom.builder()
	        .intrRoomTitle(dto.getIntrRoomTitle())
	        .host(host)
	        .statusCd(dto.getStatusCd())
	        .roomPw(dto.getRoomPw())
	        .startedDt(dto.getStartedDt())
	        .participantCount(dto.getParticipantCount())
	        .reservation(reservation) 
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
	
	// 방 참가
	@Transactional
	public void increaseCurrentParticipantCount(Integer roomId) {
	    InterviewRoom room = intrRoomRepo.findById(roomId)
	        .orElseThrow(() -> new IllegalArgumentException("방 없음"));
	    int now = (room.getCurrentParticipantCount() != null) ? room.getCurrentParticipantCount() : 0;
	    room.setCurrentParticipantCount(now + 1);
	    intrRoomRepo.save(room);
	}

	// 방 퇴장
	@Transactional
	public void decreaseCurrentParticipantCount(Integer roomId) {
	    InterviewRoom room = intrRoomRepo.findById(roomId)
	        .orElseThrow(() -> new IllegalArgumentException("방 없음"));
	    int now = (room.getCurrentParticipantCount() != null) ? room.getCurrentParticipantCount() : 0;
	    room.setCurrentParticipantCount(Math.max(0, now - 1));
	    intrRoomRepo.save(room);
	}
	
	
	
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
	            .roomPw(entity.getRoomPw())                        
	            .participantCount(entity.getParticipantCount())
	            .currentParticipantCount(entity.getCurrentParticipantCount()) 
	            .resId(entity.getReservation() != null ? entity.getReservation().getResId() : null)
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
