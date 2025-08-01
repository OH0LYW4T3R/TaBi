package com.example.tabi.treasurehunt.mytreasurehunt.vo;

import com.example.tabi.treasurehunt.mytreasurehunt.PostStatus;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyTreasureHuntDto {
    private Long myTreasureHuntId;

    private TreasureHuntPostDto treasureHuntPostDto;
    private PostStatus status;

    private LocalDateTime createdAt;
}
