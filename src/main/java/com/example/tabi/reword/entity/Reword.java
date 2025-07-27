package com.example.tabi.reword.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Reword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewordId;

    private int experience;
    private boolean type; // true = 일반 보상, false = 특별 보상
    private int creditCardCount;
    private int coin;
}
