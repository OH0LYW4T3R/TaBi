package com.example.tabi.treasurehunt.treasurehuntstartlocation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreasureHuntLocationDto {
    private Long treasureHuntLocationId;
    private String indicateLocation;
    private double latitude;
    private double longitude;
    private double altitude;
}
