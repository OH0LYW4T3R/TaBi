package com.example.tabi.quest.actions.inputpuzzleaction.vo;

import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.walkingaction.vo.WalkingActionRequest;
import com.example.tabi.quest.queststep.entity.QuestStep;
import com.example.tabi.quest.queststep.vo.QuestStepRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputPuzzleActionRequestAndHintRequest {
    private InputPuzzleActionRequest inputPuzzleActionRequest;
    private HintRequest hintRequest;

    public static InputPuzzleActionRequestAndHintRequest prepareInputPuzzleActionRequestAndHintRequest(QuestStepRequest questStepRequest, QuestStep questStep) {
        InputPuzzleActionRequest inputPuzzleActionRequest = new InputPuzzleActionRequest();
        inputPuzzleActionRequest.setAnswerString(questStepRequest.getAnswerString());
        inputPuzzleActionRequest.setCharacterImageUrl(questStepRequest.getCharacterImageUrl());
        inputPuzzleActionRequest.setQuestStep(questStep);

        HintRequest hintRequest = new HintRequest();
        hintRequest.setHintOne(questStepRequest.getHintOne());
        hintRequest.setHintTwo(questStepRequest.getHintTwo());
        hintRequest.setHintThree(questStepRequest.getHintThree());

        return new InputPuzzleActionRequestAndHintRequest(inputPuzzleActionRequest, hintRequest);
    }
}
