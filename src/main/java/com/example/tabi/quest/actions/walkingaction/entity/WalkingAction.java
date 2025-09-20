package com.example.tabi.quest.actions.walkingaction.entity;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DiscriminatorValue("WALKING")
public class WalkingAction extends Action {
    private Integer walkingCount;

    @Override
    public WalkingActionDto actionToActionDto() {
        return new WalkingActionDto(getActionId(), getCharacterImageUrl(), getQuestStep().getQuestStepId(), getWalkingCount());
    }
}
