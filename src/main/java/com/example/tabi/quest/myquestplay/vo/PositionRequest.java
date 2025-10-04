package com.example.tabi.quest.myquestplay.vo;

import lombok.Data;

@Data
public class PositionRequest {
    private Long questPostId;

    private double latitude;
    private double longitude;
}
