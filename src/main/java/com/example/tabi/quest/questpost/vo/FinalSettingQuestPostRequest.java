package com.example.tabi.quest.questpost.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FinalSettingQuestPostRequest {
    private Long questPostId;

    private String questTitle;
    private String questDescription;

    // 예상 소요시간
    private Integer estimatedDay;
    private Integer estimatedHour;
    private Integer estimatedMinute;
    // 예상 소요시간

    private boolean locked;
    private boolean pub;
}
