package com.example.tabi.quest.actions.talkingaction.vo;

import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionRequest;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TalkingActionRequest {
    private String story;
    private String characterImageUrl;
    private QuestStep questStep;

    public static TalkingActionRequest prepareTalkingActionRequest(QuestStepRequest questStepRequest, QuestStep questStep) {
        TalkingActionRequest talkingActionRequest = new TalkingActionRequest();
        talkingActionRequest.setStory(questStepRequest.getStory());
        talkingActionRequest.setCharacterImageUrl(questStepRequest.getCharacterImageUrl());
        talkingActionRequest.setQuestStep(questStep);

        return talkingActionRequest;
    }
}
