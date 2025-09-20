package com.example.tabi.quest.actions.walkingaction.vo;

import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.Data;

@Data
public class WalkingActionRequest {
    private Integer walkingCount;
    private String characterImageUrl;
    private QuestStep questStep;
}
