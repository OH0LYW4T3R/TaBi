package com.example.tabi.quest.actions.walkingaction.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.vo.ActionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalkingActionDto extends ActionDto {
    private Integer walkingCount;

    public WalkingActionDto(Long actionId, String characterImageUrl, Long questStepId, Integer walkingCount) {
        super(actionId, characterImageUrl, questStepId);
        this.walkingCount = walkingCount;
    }
}
