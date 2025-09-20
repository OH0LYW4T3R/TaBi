package com.example.tabi.quest.queststep.vo;

import com.example.tabi.quest.actions.action.vo.ActionDto;
import lombok.Data;

@Data
public class QuestStepDto {
    private Long questStepId;
    private ActionDto actionDto;
    private Long questIndicatingId;
}
