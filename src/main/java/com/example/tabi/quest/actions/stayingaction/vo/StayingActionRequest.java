package com.example.tabi.quest.actions.stayingaction.vo;

import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.Data;

@Data
public class StayingActionRequest {
    private Integer day;
    private Integer hour;
    private Integer minute;
    private String characterImageUrl;
    private QuestStep questStep;
}
