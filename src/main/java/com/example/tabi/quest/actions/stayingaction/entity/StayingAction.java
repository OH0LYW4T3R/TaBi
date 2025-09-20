package com.example.tabi.quest.actions.stayingaction.entity;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.stayingaction.vo.StayingActionDto;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("STAYING")
public class StayingAction extends Action {
    private Integer day;
    private Integer hour;
    private Integer minute;

    @Override
    public StayingActionDto actionToActionDto() {
        return new StayingActionDto(getActionId(), getCharacterImageUrl(), getQuestStep().getQuestStepId(), getDay(), getHour(), getMinute());
    }
}
