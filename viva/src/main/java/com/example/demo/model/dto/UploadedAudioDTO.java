package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedAudioDTO {
    private Integer recodeId;
    private Integer roomId;
    private String userId;
    private String filename;
    private String filePath;
    private LocalDateTime uploadedAt;
}
