package com.example.tabi.treasurehunt.treasurehuntpost.vo;

import lombok.Data;

@Data
public class TreasureHuntPostRequest {
    private Boolean isPublic;
    private String treasureHuntTitle;
    private String treasureHuntDescription;

    private String imageUrl;

    private double latitude;
    private double longitude;
    private double altitude;
}
