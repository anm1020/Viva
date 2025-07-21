package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.dto.UploadedAudioDTO;
import com.example.demo.model.entity.InterviewRoom;
import com.example.demo.model.entity.UploadedAudio;
import com.example.demo.model.entity.Users;
import com.example.demo.repository.InterviewRoomRepository;
import com.example.demo.repository.UploadedAudioRepository;
import com.example.demo.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadedAudioService {
	
	private final UploadedAudioRepository uploadedAudioRepository;
    private final InterviewRoomRepository interviewRoomRepository;
    private final UsersRepository userRepository;
    
    public UploadedAudio save(String filename, String fullPath, int roomId, String userId) {
        InterviewRoom room = interviewRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 면접방이 존재하지 않습니다: " + roomId));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다: " + userId));

        UploadedAudio audio = UploadedAudio.builder()
                .filename(filename)
                .filePath(fullPath)
                .uploadedAt(LocalDateTime.now())
                .interviewRoom(room)
                .user(user)
                .build();
        return uploadedAudioRepository.save(audio);
    }
    
	// 방번호로 검색
    public List<UploadedAudioDTO> findByRoomId(Integer roomId) {
        return uploadedAudioRepository.findByInterviewRoom_IntrRoomId(roomId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 유저 ID로 검색
    public List<UploadedAudioDTO> findByUserId(String userId) {
        return uploadedAudioRepository.findByUser_UserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 둘다로 검색
    public List<UploadedAudioDTO> findByRoomIdAndUserId(Integer roomId, String userId) {
        return uploadedAudioRepository.findByInterviewRoom_IntrRoomIdAndUser_UserId(roomId, userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // entity -> DTO 변환
    private UploadedAudioDTO toDto(UploadedAudio audio) {
        return UploadedAudioDTO.builder()
                .recodeId(audio.getRecodeId())
                .roomId(audio.getInterviewRoom().getIntrRoomId())
                .userId(audio.getUser().getUserId())
                .filename(audio.getFilename())
                .filePath(audio.getFilePath())
                .uploadedAt(audio.getUploadedAt())
                .build();
    }
}
