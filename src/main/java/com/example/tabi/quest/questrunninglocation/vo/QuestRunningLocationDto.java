package com.example.tabi.quest.questrunninglocation.vo;

import com.example.tabi.quest.questrunninglocation.entity.QuestRunningLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestRunningLocationDto {
    private Long questRunningLocationId;

    private Integer sequence; // 순서 번호
    private String locationName; // 보여줄 주소 (ex 한밭대학교)
    private String detailLocation; // 실 주소 (ex 대전광역시 유성구 ...)

    private double latitude;
    private double longitude;
    private double altitude;

    private Long questId;

    public static QuestRunningLocationDto questRunningLocationToQuestRunningLocationDto(QuestRunningLocation questRunningLocation) {
        QuestRunningLocationDto questRunningLocationDto = new QuestRunningLocationDto();
        questRunningLocationDto.setQuestRunningLocationId(questRunningLocation.getQuestRunningLocationId());
        questRunningLocationDto.setSequence(questRunningLocation.getSequence());
        questRunningLocationDto.setLocationName(questRunningLocation.getLocationName());
        questRunningLocationDto.setDetailLocation(questRunningLocation.getDetailLocation());
        questRunningLocationDto.setLatitude(questRunningLocation.getLatitude());
        questRunningLocationDto.setLongitude(questRunningLocation.getLongitude());
        questRunningLocationDto.setAltitude(questRunningLocation.getAltitude());
        questRunningLocationDto.setQuestId(questRunningLocation.getQuest().getQuestId());

        return questRunningLocationDto;
    }
}
