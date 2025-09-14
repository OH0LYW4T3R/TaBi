package com.example.tabi.quest.actions.inputpuzzleaction.vo;

import com.example.tabi.quest.actions.ActionType;
import com.example.tabi.quest.actions.action.vo.ActionDto;
import com.example.tabi.quest.actions.hint.vo.HintDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputPuzzleActionDto extends ActionDto {
    private String answerString;
    private HintDto hintDto;

    public InputPuzzleActionDto(Long actionId, String characterImageUrl, Long questStepId, String answerString, HintDto hintDto) {
        super(actionId, characterImageUrl, questStepId);
        this.answerString = answerString;
        this.hintDto = hintDto;
    }
}
