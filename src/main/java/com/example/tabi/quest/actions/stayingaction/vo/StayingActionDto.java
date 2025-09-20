package com.example.tabi.quest.actions.stayingaction.vo;

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
public class StayingActionDto extends ActionDto {
    private Integer day;
    private Integer hour;
    private Integer minute;

    public StayingActionDto(Long actionId, String characterImageUrl, Long questStepId, Integer day, Integer hour, Integer minute) {
        super(actionId, characterImageUrl, questStepId);
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
}
