package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "intr_disabled")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrDisabled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dis_id")
    private Long disId;

    @Column(name = "intr_id", nullable = false)
    private String intrId;

    @Column(name = "disabled_date", nullable = false)
    private String disabledDate;

    @Column(name = "disabled_time", nullable = false)
    private String disabledTime;

    // 필요시 연관관계 매핑도 추가 가능 (단방향 예시)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "intr_id", insertable = false, updatable = false)
    // private Users interviewer;
}
