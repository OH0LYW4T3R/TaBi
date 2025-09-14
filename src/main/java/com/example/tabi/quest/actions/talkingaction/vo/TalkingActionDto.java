package com.example.tabi.quest.actions.talkingaction.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.entity.Action;
import com.example.tabi.quest.actions.action.vo.ActionDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TalkingActionDto extends ActionDto {
    private String story;

    public TalkingActionDto(Long actionId, String characterImageUrl, Long questStepId, String story) {
        super(actionId, characterImageUrl, questStepId);
        this.story = story;
    }
}
