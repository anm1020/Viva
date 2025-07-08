package com.example.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPoint {

    @Id
    private String userId;

    private int point;
}