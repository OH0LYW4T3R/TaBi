package com.example.tabi.quest.actions.action.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.entity.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDto {
    private Long actionId;
    private String characterImageUrl;
    private Long questStepId;
}
