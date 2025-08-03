package com.example.tabi.treasurehunt.mytreasurehuntplay.vo;

import com.example.tabi.treasurehunt.mytreasurehuntplay.PlayStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyTreasureHuntPlayDto {
    private Long myTreasureHuntId;
    private Long appUserId;
    private Long treasureHuntPostId;
    private PlayStatus playStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String errorMessage;
}
