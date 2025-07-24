package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.UploadedAudioService;

@RestController
@RequestMapping("/api/audio")
public class AudioUploadController {

	private final String baseUploadPath;
	private final UploadedAudioService uploadedAudioService;
	
	public AudioUploadController(UploadedAudioService uploadedAudioService) {
		this.uploadedAudioService = uploadedAudioService;
		
		// EC2에서는 /home/ubuntu/uploads/audio 절대경로
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win") || osName.contains("mac")) {
        	this.baseUploadPath = System.getProperty("user.dir") + "/recordings/audio/";
 // 로컬 개발용 window에서 저장
        } else {
            this.baseUploadPath = "/home/ubuntu/recordings/audio/"; // EC2 리눅스 서버용
        }
	}
	
	@PostMapping("/upload")
    public ResponseEntity<String> uploadAudio(@RequestParam("audioFile") MultipartFile audioFile) {
        try {
            // 저장 파일 이름 생성
            String filename = UUID.randomUUID() + "_" + audioFile.getOriginalFilename();

            // 디렉토리 생성 및 파일 저장
            Path savePath = Paths.get(baseUploadPath + filename);
            Files.createDirectories(savePath.getParent());
            Files.write(savePath, audioFile.getBytes());

            // TODO: DB에 filename 또는 전체 savePath 저장
            return ResponseEntity.ok("파일 업로드 성공: " + filename);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패: " + e.getMessage());
        }
    }
	
}
