package com.example.tabi.quest.queststartlocation.vo;

import com.example.tabi.quest.queststartlocation.entity.QuestStartLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestStartLocationDto {
    private Long questStartLocationId;
    private String actualLocation;
    private String indicateLocation;
    private double latitude;
    private double longitude;
    private double altitude;
    private Long questPostId;

    public static QuestStartLocationDto questStartLocationToQuestStartLocationDto(QuestStartLocation questStartLocation) {
        return new QuestStartLocationDto(
                questStartLocation.getQuestStartLocationId(),
                questStartLocation.getActualLocation(),
                questStartLocation.getIndicateLocation(),
                questStartLocation.getLatitude(),
                questStartLocation.getLongitude(),
                questStartLocation.getAltitude(),
                questStartLocation.getQuestPost().getQuestPostId()
        );
    }
}
