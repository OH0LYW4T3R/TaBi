package com.example.tabi.treasurehunt.mytreasurehuntplay.vo;

import lombok.Data;

@Data
public class PositionRequest {
    private Long treasureHuntPostId;

    private double latitude;
    private double longitude;
}
