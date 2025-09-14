package com.example.tabi.quest.actions.talkingaction.entity;

import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.action.vo.ActionDto;
import com.example.tabi.quest.actions.talkingaction.vo.TalkingActionDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("TALKING")
public class TalkingAction extends Action {
    private String story;

    @Override
    public TalkingActionDto actionToActionDto() {
        return new TalkingActionDto(getActionId(), getCharacterImageUrl(), getQuestStep().getQuestStepId(), getStory());
    }
}
