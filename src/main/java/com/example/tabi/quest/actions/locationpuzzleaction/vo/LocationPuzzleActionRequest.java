package com.example.tabi.quest.actions.locationpuzzleaction.vo;

import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationPuzzleActionRequest {
    private String locationName;
    private String actualLocation;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private String characterImageUrl;
    private QuestStep questStep;
}
