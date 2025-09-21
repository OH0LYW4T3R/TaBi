package com.example.tabi.quest.questrunninglocation.vo;

import lombok.Data;

@Data
public class QuestRunningLocationRequest {
    private Long questId;

    private Integer sequence; // 순서 번호
    private String locationName; // 보여줄 주소 (ex 한밭대학교)
    private String detailLocation; // 실 주소 (ex 대전광역시 유성구 ...)

    private double latitude;
    private double longitude;
    private double altitude;
}
