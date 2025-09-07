package com.example.tabi.quest.queststartlocation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestStartLocationDto {
    private Long questStartLocationId;
    private String actualLocation;
    private double latitude;
    private double longitude;
    private double altitude;
    private Long questPostId;
}
