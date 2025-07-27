package com.example.tabi.postcounter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PostCounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postCounterId;

    private int likeCount;
    private int playCount;
    private int shareCount;
    private int commentCount;
    private int reportCount;
}
