package com.example.tabi.quest.actions.locationpuzzleaction.vo;

import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.Data;

@Data
public class LocationPuzzleActionRequest {
    private String locationName;
    private String actualLocation;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private String characterImageUrl;
    private QuestStep questStep;
}
