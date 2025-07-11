package com.example.demo.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "text_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextRoom {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "text_room_id")
    private Integer textRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intr_room_id", nullable = false)
    private InterviewRoom intrRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Users host;

    @Column(name = "text_room_title", length = 200)
    private String textRoomTitle;

    @Column(name = "created_dt")
    private LocalDateTime createdDt;

    @Column(name = "status_cd", length = 20)
    private String statusCd;

    @PrePersist
    public void prePersist() {
        this.createdDt = LocalDateTime.now();
    }
}
