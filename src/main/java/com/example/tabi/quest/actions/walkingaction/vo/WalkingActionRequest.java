package com.example.tabi.quest.actions.walkingaction.vo;

import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionRequest;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import lombok.Data;

@Data
public class WalkingActionRequest {
    private Integer walkingCount;
    private String characterImageUrl;
    private QuestStep questStep;

    public static WalkingActionRequest prepareWalkingActionRequest(QuestStepRequest questStepRequest, QuestStep questStep) {
        WalkingActionRequest walkingActionRequest = new WalkingActionRequest();
        walkingActionRequest.setWalkingCount(questStepRequest.getWalkingCount());
        walkingActionRequest.setCharacterImageUrl(questStepRequest.getCharacterImageUrl());
        walkingActionRequest.setQuestStep(questStep);

        return walkingActionRequest;
    }
}
