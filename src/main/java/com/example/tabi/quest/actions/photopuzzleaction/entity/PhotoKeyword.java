package com.example.tabi.quest.actions.photopuzzleaction.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PhotoKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoKeywordId;

    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_puzzle_action_id")
    private PhotoPuzzleAction photoPuzzleAction;
}
