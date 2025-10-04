package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.vo;

import com.example.tabi.quest.myquestplay.trace.questcurrentpoint.entity.QuestCurrentPoint;
import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestNextLocationDto {
    private Long questRunningLocationId;

    private String locationName; // 보여줄 주소 (ex 한밭대학교)

    private double latitude;
    private double longitude;
    private double altitude;

    private Boolean endLocation;

    private String errorMessage;

    public static QuestNextLocationDto createQuestNextLocationDto(QuestCurrentPoint questCurrentPoint, QuestRunningLocation questRunningLocation) {
        Boolean endLocation;

        if (Objects.equals(questCurrentPoint.getCurrentQuestRunningLocationIndex(), questCurrentPoint.getEndQuestRunningLocationIndex()))
            endLocation = true;
        else endLocation = false;

        return new QuestNextLocationDto(
                questRunningLocation.getQuestRunningLocationId(),
                questRunningLocation.getLocationName(),
                questRunningLocation.getLatitude(),
                questRunningLocation.getLongitude(),
                questRunningLocation.getAltitude(),
                endLocation,
                null
        );
    }
}
