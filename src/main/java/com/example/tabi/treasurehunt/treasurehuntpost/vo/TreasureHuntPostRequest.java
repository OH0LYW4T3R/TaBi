package com.example.tabi.treasurehunt.treasurehuntpost.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TreasureHuntPostRequest {
    private Boolean isPublic;
    private String treasureHuntTitle;
    private String treasureHuntDescription;

    private MultipartFile image;

    private double latitude;
    private double longitude;
    private double altitude;
}
