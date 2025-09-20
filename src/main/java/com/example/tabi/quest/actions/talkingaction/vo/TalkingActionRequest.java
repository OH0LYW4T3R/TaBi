package com.example.tabi.quest.actions.talkingaction.vo;

import com.example.tabi.quest.queststep.entity.QuestStep;
import lombok.Data;

@Data
public class TalkingActionRequest {
    private String story;
    private String characterImageUrl;
    private QuestStep questStep;
}
