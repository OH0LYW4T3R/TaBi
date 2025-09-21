package com.example.tabi.quest.actions.stayingaction.vo;

import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionRequest;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StayingActionRequest {
    private Integer day;
    private Integer hour;
    private Integer minute;
    private String characterImageUrl;
    private QuestStep questStep;

    public static StayingActionRequest prepareStayingActionRequest(QuestStepRequest questStepRequest, QuestStep questStep) {
        StayingActionRequest stayingActionRequest = new StayingActionRequest();
        stayingActionRequest.setDay(questStepRequest.getDay());
        stayingActionRequest.setHour(questStepRequest.getHour());
        stayingActionRequest.setMinute(questStepRequest.getMinute());
        stayingActionRequest.setCharacterImageUrl(questStepRequest.getCharacterImageUrl());
        stayingActionRequest.setQuestStep(questStep);

        return stayingActionRequest;
    }
}
