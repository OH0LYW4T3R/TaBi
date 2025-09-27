package com.example.tabi.quest.questpost.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FinalSettingQuestPostRequest {
    private Long questPostId;

    private String questTitle;
    private String questDescription;

    private LocalDateTime estimatedTime;

    private boolean locked;
    private boolean pub;
}
